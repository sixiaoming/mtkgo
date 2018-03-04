package com.uuzu.mktgo.service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.elasticsearch.PersonaSummaryppingRepository;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.OperationEnum;
import com.uuzu.mktgo.pojo.RegionDistributionModel;

@Service
@Slf4j
public class RegionDistributionService {

    private static final String           NIL_STR     = "NIL";
    private static final String[]         FIELDS      = { "income", "sysver", "brand_name", "carrier", 
                                                          "segment", "agebin", "network", "occupation", 
                                                          "edu", "price", "house", "screensize", "car", 
                                                          "married", "kids","gender"};
    private static final String           HBASE_TABLE = "persona_summary_prod";
    @Autowired
    private PersonaSummaryppingRepository personaSummaryppingRepository;
    @Autowired
    private HbaseService                  hbaseService;
    @Autowired
    DateMonthMapper                       dateMonthMapper;
    @Autowired
    DictInfoService                       dictInfoService;
    @Autowired
    private ThreadPoolTaskExecutor        threadPoolTaskExecutor;

    /**
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param date
     * @param province
     * @return
     * @throws Exception
     */
    public RegionDistributionModel regionDistribution(String brand, String model, String price, String country, String date, String province) throws Exception {
        RegionDistributionModel regionDistributionModel = new RegionDistributionModel();
        // default country=cn
        if (StringUtils.isEmpty(country)) country = "cn";
        Map<String, Map<String, String>> list = new HashMap<String, Map<String, String>>();
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);
        PersonaSummary personaSummary = new PersonaSummary(model, brand, price, country, province, date);
        if (StringUtils.isEmpty(date)) {
            // get max month
            String maxMonth = dateMonthMapper.queryMaxMonth();
            personaSummary.setMnt(maxMonth);
            completionService.submit(new RegionDistributionService.Task(getRowKeyBoolQuery(personaSummary, "mnt", OperationEnum.EQ.getOperation()), "mnt", "imei_count", list, "sumMap"));
            // query province
            completionService.submit(new RegionDistributionService.Task(getProvinceRowKeyBoolQuery(personaSummary, OperationEnum.EQ.getOperation()), "province", "imei_count", list, "sumProvinceMap"));
            // query province marketShare
            PersonaSummary marketSharePersonaSummary = new PersonaSummary(null, null, null, country, null, maxMonth);
            completionService.submit(new RegionDistributionService.Task(getProvinceRowKeyBoolQuery(marketSharePersonaSummary, OperationEnum.EQ.getOperation()), "province", "imei_count", list, "sumMapByCountry"));
        } else {
            completionService.submit(new RegionDistributionService.Task(getRowKeyBoolQuery(personaSummary, "mnt", OperationEnum.EQ.getOperation()), "mnt", "imei_count_incr", list, "sumMap"));
            // query province
            completionService.submit(new RegionDistributionService.Task(getProvinceRowKeyBoolQuery(personaSummary, OperationEnum.EQ.getOperation()), "province", "imei_count_incr", list, "sumProvinceMap"));
            // query province marketShare
            PersonaSummary marketSharePersonaSummary = new PersonaSummary(null, null, null, country, null, date);
            completionService.submit(new RegionDistributionService.Task(getProvinceRowKeyBoolQuery(marketSharePersonaSummary, OperationEnum.EQ.getOperation()), "province", "imei_count_incr", list, "sumMapByCountry"));
        }

        for (int i = 0; i < 3; i++) {
            Future<String> future = completionService.take();
            // log.info("Tread======" + future.get());
        }

        if (list.size() == 0) return regionDistributionModel;

        Map<String, String> sumMap = list.get("sumMap");
        Map<String, String> sumProvinceMap = list.get("sumProvinceMap");
        Map<String, String> sumMapByCountry = list.get("sumMapByCountry");
        convertProvinceModel(regionDistributionModel, sumProvinceMap, sumMap);
        convertCountryModel(regionDistributionModel, sumProvinceMap, sumMapByCountry);

        return regionDistributionModel;
    }

    /**
     * @param regionDistributionModel
     * @param filterMap
     * @param sumMap
     * @return
     */
    private RegionDistributionModel convertCountryModel(RegionDistributionModel regionDistributionModel, Map<String, String> filterMap, Map<String, String> sumMap) {

        if (MapUtils.isEmpty(filterMap) || MapUtils.isEmpty(sumMap)) return regionDistributionModel;

        // 获取所有省份的字典
        Map<String, String> dictInfo = dictInfoService.getProvinceInfo();

        // 计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (String key : filterMap.keySet()) {
            if (StringUtils.equals("unknown", key)) continue;
            baseModels.add(new BaseModel(dictInfo.get(key), Double.parseDouble(filterMap.get(key)) / Double.parseDouble(sumMap.get(key))));
        }

        Collections.sort(baseModels, new Comparator<BaseModel>() {

            @Override
            public int compare(BaseModel o1, BaseModel o2) {
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else if (o1.getValue() == o2.getValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        regionDistributionModel.setProvinceMarketShare(baseModels);
        return regionDistributionModel;
    }

    /**
     * setProvince
     * 
     * @param regionDistributionModel
     * @param map
     * @param sumMap
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private RegionDistributionModel convertProvinceModel(RegionDistributionModel regionDistributionModel, Map<String, String> map, Map<String, String> sumMap) {

        if (MapUtils.isEmpty(map) || MapUtils.isEmpty(sumMap)) return regionDistributionModel;
        // 获取所有省份的字典
        Map<String, String> dictInfo = dictInfoService.getProvinceInfo();

        long sum = 0l;
        // sum
        for (String key : sumMap.keySet()) {
            sum += Long.parseLong(sumMap.get(key));
        }

        // 计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (String key : map.keySet()) {
            if (StringUtils.equals("unknown", key)) continue;
            baseModels.add(new BaseModel(dictInfo.get(key), Double.parseDouble(map.get(key)) / sum));
        }

        Collections.sort(baseModels, new Comparator<BaseModel>() {

            @Override
            public int compare(BaseModel o1, BaseModel o2) {
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else if (o1.getValue() == o2.getValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        regionDistributionModel.setProvince(baseModels);
        return regionDistributionModel;
    }

    /**
     * @param personaSummary
     * @param operation
     * @return
     */
    private BoolQueryBuilder getProvinceRowKeyBoolQuery(PersonaSummary personaSummary, String operation) {
        BoolQueryBuilder boolQueryBuilder = generateBoolQuery(personaSummary, operation);

        for (String field : FIELDS) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NIL_STR));
        }
        return boolQueryBuilder;

    }

    /**
     * @param personaSummary
     * @param operation
     * @return
     */
    private BoolQueryBuilder generateBoolQuery(PersonaSummary personaSummary, String operation) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", personaSummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", personaSummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NIL_STR));
        }

        // group by province province must not nil
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("province", NIL_STR));
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("province", "unknown"));
        }

        return boolQueryBuilder;
    }

    /**
     * @param personaSummary
     * @param countfield
     * @param operation
     * @return
     */
    private BoolQueryBuilder getRowKeyBoolQuery(PersonaSummary personaSummary, String countfield, String operation) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", personaSummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", personaSummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NIL_STR));
        }

        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NIL_STR));
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("province", "unknown"));
        }

        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NIL_STR));
        }

        for (String field : FIELDS) {
            if (field.equals(countfield)) {
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, NIL_STR));
                // boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "-1"));
                // boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "unknown"));
            } else {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NIL_STR));
            }
        }
        return boolQueryBuilder;

    }

    private String getValueByField(PersonaSummary personaSummary, String countfield) throws Exception {
        Field field = personaSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(personaSummary).toString();
    }

    /**
     * task
     */
    class Task implements Callable {

        private BoolQueryBuilder                 boolQueryBuilder;
        private String                           attrField;
        private String                           countField;
        private String                           key;
        private Map<String, Map<String, String>> resultMap;

        public Task(BoolQueryBuilder boolQueryBuilder, String attrField, String countField, Map<String, Map<String, String>> resultMap, String key) {
            this.boolQueryBuilder = boolQueryBuilder;
            this.attrField = attrField;
            this.countField = countField;
            this.resultMap = resultMap;
            this.key = key;
        }

        @Override
        public String call() {
            try {

                Pageable pageable = new PageRequest(0, 1000);
                SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(boolQueryBuilder).build();

                Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
                Map<String, String> result = new HashMap<>();
                for (PersonaSummary summary : search) {
                    result.put(getValueByField(summary, attrField), summary.getRow_key().replace("\u0000", "\u0001"));
                }

                Map<String, String> hbaseResult = hbaseService.getResultByHbaseTable(result, countField, HBASE_TABLE);
                resultMap.put(key, hbaseResult);

                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;

        }
    }

}
