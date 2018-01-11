package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.elasticsearch.PersonaSummaryppingRepository;
import com.uuzu.mktgo.mapper.BrandModelMappingNewMapper;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.*;
import com.uuzu.mktgo.util.DateUtil;
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
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

@Service
@Slf4j
public class OverviewBrandService {

    private static final String HBASE_TABLE = "persona_summary_prod";
    private static final String NULLSRT = "NIL";
    private static final String SPLIT_SYMBOL = "@";//分子
    private static final String SPLIT_SYMBOL2="#";

    @Autowired
    BrandModelMappingNewMapper brandModelMappingNewMapper;

    @Autowired
    private HbaseService hbaseService;
    @Autowired
    DateMonthMapper dateMonthMapper;

    @Autowired
    PersonaSummaryppingRepository personaSummaryppingRepository;
    @Autowired
    DictInfoService dictInfoService;

    @Autowired
    PicService picService;

    /**
     *
     * @param brand
     * @param price
     * @param country
     * @param province
     * @param date
     * @return
     * @throws Exception
     */
    public OverviewModel overviewBrand(String brand, String price, String country, String province, String date) throws Exception{
//        long start = System.currentTimeMillis();
        OverviewModel overviewModel = new OverviewModel();

        Map<String, Map<String, String>> list = new HashMap<String, Map<String, String>>();
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);
        String[] sixMonths = null;
        if (StringUtils.isBlank(date)) {
            date = dateMonthMapper.queryMaxMonth();
            // holdingRankRowKey
            PersonaSummary personaSummary = new PersonaSummary(null, brand, price, country, province, date);
            // 将查询出的row key在hbase查询出值按imei_count降序 取出brand的所在位置。
            completionService.submit(new OverviewBrandService.Task(getHoldingRankBoolQuery(generateBoolQuery(personaSummary, "brand", OperationEnum.EQ.getOperation())),
                    "brand", "imei_count", 1000,  list, "holdingRank"));
            sixMonths = DateUtil.getLastSixMonthDate(date, 6);
            for (String dateStr : sixMonths) {
                PersonaSummary personaSummaryByDate = new PersonaSummary(null, brand, price, country, province, dateStr);
                completionService.submit(new OverviewBrandService.Task(getSumByMonthBoolQuery(generateBoolQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation())),
                        "mnt", "imei_count", 1000, list, dateStr));
                // sumRowKeysByCondition
                completionService.submit(new OverviewBrandService.Task(getSumByConditionBoolQuery(generateBoolQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation()), brand),
                        "mnt", "imei_count", 1000, list, dateStr + SPLIT_SYMBOL));
                //计算unknown
                completionService.submit(new OverviewBrandService.Task(generateBoolForUnknownQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation()),
                        "mnt","imei_count",1000,list,dateStr + SPLIT_SYMBOL2));

            }
            // hot sell rank
            completionService.submit(new OverviewBrandService.Task(getHotSellRankBoolQuery(generateBoolQuery(personaSummary, "model", OperationEnum.EQ.getOperation()), brand),
                    "model", "imei_count", 1000, list, "hotSellMap"));

            // px rank
            completionService.submit(new OverviewBrandService.Task(getPxRankBoolQuery(generateBoolQuery(personaSummary, "screensize", OperationEnum.EQ.getOperation()), brand),
                    "screensize", "imei_count", 10000, list, "pxRank"));

            // os rank
            completionService.submit(new OverviewBrandService.Task(getOsRankBoolQuery(generateBoolQuery(personaSummary, "sysver", OperationEnum.EQ.getOperation()), brand),
                    "sysver", "imei_count", 1000, list, "osRank"));

        } else {

            PersonaSummary personaSummary = new PersonaSummary(null, brand, price, country, province, date);

            completionService.submit(new OverviewBrandService.Task(getHoldingRankBoolQuery(generateBoolQuery(personaSummary, "brand", OperationEnum.EQ.getOperation())),
                    "brand", "imei_count_incr", 1000,  list, "holdingRank"));
            sixMonths = DateUtil.getLastSixMonthDate(date, 6);
            for (String dateStr : sixMonths) {
                PersonaSummary personaSummaryByDate = new PersonaSummary(null, brand, price, country, province, dateStr);
                completionService.submit(new OverviewBrandService.Task(getSumByMonthBoolQuery(generateBoolQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation())),
                        "mnt", "imei_count_incr", 1000, list, dateStr));
                // sumRowKeysByCondition
                completionService.submit(new OverviewBrandService.Task(getSumByConditionBoolQuery(generateBoolQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation()), brand),
                        "mnt", "imei_count_incr", 1000, list, dateStr + SPLIT_SYMBOL));
                completionService.submit(new OverviewBrandService.Task(generateBoolForUnknownQuery(personaSummaryByDate, "mnt", OperationEnum.EQ.getOperation()),
                        "mnt","imei_count_incr",1000,list,dateStr + SPLIT_SYMBOL2));
            }

            completionService.submit(new OverviewBrandService.Task(getHotSellRankBoolQuery(generateBoolQuery(personaSummary, "model", OperationEnum.EQ.getOperation()), brand),
                    "model", "imei_count_incr", 1000, list, "hotSellMap"));

            completionService.submit(new OverviewBrandService.Task(getPxRankBoolQuery(generateBoolQuery(personaSummary, "screensize", OperationEnum.EQ.getOperation()), brand),
                    "screensize", "imei_count_incr", 10000, list, "pxRank"));

            completionService.submit(new OverviewBrandService.Task(getOsRankBoolQuery(generateBoolQuery(personaSummary, "sysver", OperationEnum.EQ.getOperation()), brand),
                    "sysver", "imei_count_incr", 1000, list, "osRank"));
        }
        for (int i = 0; i < 22;i++) {
            Future<String> future = completionService.take();
//            log.info("thread=====" + future.get());
        }
        setOverviewModelAttribute(overviewModel, list, brand, date, sixMonths);
       return overviewModel;
    }

    /**
     *
     * @param overviewModel
     * @param resultList
     * @param brand
     * @param date
     * @param sixMonths
     */
    private void setOverviewModelAttribute(OverviewModel overviewModel, Map<String, Map<String, String>> resultList, String brand, String date, String[] sixMonths) {

        Map<String, String> holdingRankMap = resultList.get("holdingRank");
        Map<String, String> hotSellRankMap = resultList.get("hotSellMap");
        Map<String, String> pxRankMap = resultList.get("pxRank");
        Map<String, String> osRankMap = resultList.get("osRank");

        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(holdingRankMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                if (Long.valueOf(o2.getValue()) - Long.valueOf(o1.getValue()) > 0) {
                    return 1;
                } else if (Long.valueOf(o1.getValue()) == Long.valueOf(o2.getValue())) {
                    return 0;
                } else {
                    return -1;
                }
            }

        });
        int holding = 0;
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, String> entry = list.get(i);
            if (StringUtils.equals(entry.getKey(), brand)) holding = i + 1;
        }

        overviewModel.setHolding(holding);


        // top 10 此处需要减少一个unknown
        List<OverviewBaseModel> market_share_brand = new ArrayList<>();
        for (String key : sixMonths) {
            String unknownKey = key + SPLIT_SYMBOL2;
            Map<String,String> unknownMap = resultList.get(unknownKey);
            Double unknomnValue = null;
            if(MapUtils.isEmpty(unknownMap)){
                unknomnValue = 0.0;
            }else{
                 unknomnValue = Double.parseDouble(unknownMap.get(key));
            }

            if (resultList.get(key + SPLIT_SYMBOL).get(key) == null || resultList.get(key).get(key) == null ||
                    StringUtils.equals(resultList.get(key + SPLIT_SYMBOL).get(key), "null") || StringUtils.equals(resultList.get(key).get(key), "null"))
                continue;
            market_share_brand.add(new OverviewBaseModel(key, Double.valueOf(resultList.get(key + SPLIT_SYMBOL).get(key)) / (Double.valueOf(resultList.get(key).get(key)) - unknomnValue)));
        }


        List<OverviewBaseListModel> market_share_brands = new ArrayList<>();
        market_share_brands.add(new OverviewBaseListModel(brand, market_share_brand));
        overviewModel.setMarket_share_brand(market_share_brands);

        overviewModel.setMarket_share_rank(resultList.get(date + SPLIT_SYMBOL).get(date) == null || resultList.get(date).get(date) == null ||
                StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") || StringUtils.equals(resultList.get(date).get(date), "null") ?
                0 : Double.valueOf(resultList.get(date + SPLIT_SYMBOL).get(date)) /
                (Double.valueOf(resultList.get(date).get(date)) - Double.valueOf((resultList.get(date + SPLIT_SYMBOL2) == null || resultList.get(date + SPLIT_SYMBOL2).get(date) == null) ? "0.0" : resultList.get(date + SPLIT_SYMBOL2).get(date))));

        // top 10
        List<BaseModel> hotSellBaseModels = new ArrayList<BaseModel>();
        for (String key : hotSellRankMap.keySet()) {
            hotSellBaseModels.add(new BaseModel(key, hotSellRankMap.get(key) == null || resultList.get(date + SPLIT_SYMBOL).get(date) == null ||
                    StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") || StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") ?
                    0 : Double.valueOf(hotSellRankMap.get(key)) / Double.valueOf(resultList.get(date + SPLIT_SYMBOL).get(date))));
        }
        overviewModel.setHot_selling_rank(getTop10(hotSellBaseModels));

        // top 10
        List<BaseModel> pxBaseModels = new ArrayList<BaseModel>();
        for (String key : pxRankMap.keySet()) {
            pxBaseModels.add(new BaseModel(key, pxRankMap.get(key) == null || resultList.get(date + SPLIT_SYMBOL).get(date) == null ||
                    StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") || StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") ?
                    0 : Double.valueOf(pxRankMap.get(key)) / Double.valueOf(resultList.get(date + SPLIT_SYMBOL).get(date))));
        }
        overviewModel.setPx_rank(getTop10(pxBaseModels));

        // top 10
        List<BaseModel> osBaseModels = new ArrayList<BaseModel>();
        for (String key : osRankMap.keySet()) {
            osBaseModels.add(new BaseModel(key, osRankMap.get(key) == null || resultList.get(date + SPLIT_SYMBOL).get(date) == null ||
                    StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") || StringUtils.equals(resultList.get(date + SPLIT_SYMBOL).get(date), "null") ?
                    0 : Double.valueOf(osRankMap.get(key)) / Double.valueOf(resultList.get(date + SPLIT_SYMBOL).get(date))));
        }
        overviewModel.setOs_rank(getTop10(osBaseModels));
        overviewModel.setBrand(brand);
        overviewModel.setBrandPic(picService.getPicUrl(brand));
        resultList = null;

    }


    /**
     *
     * @param list
     * @return
     */
    private List<BaseModel> getTop10 (List<BaseModel> list){
        if (CollectionUtils.isEmpty(list)) return list;
        Collections.sort(list, new Comparator<BaseModel>() {
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
        if (list.size() > 10) {
            list = list.subList(0, 10);
        }
        return list;

    }




    /**
     *
     * @param boolQueryBuilder
     * @param brand
     * @return
     */
    private BoolQueryBuilder getOsRankBoolQuery(BoolQueryBuilder boolQueryBuilder, String brand) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", brand));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }
        return boolQueryBuilder;
    }


    /**
     *
     * @param boolQueryBuilder
     * @param brand
     * @return
     */
    private BoolQueryBuilder getUnknownQuery(BoolQueryBuilder boolQueryBuilder, String brand) {

        return boolQueryBuilder;
    }

    /**
     *
     * @param boolQueryBuilder
     * @param brand
     * @return
     */
    private BoolQueryBuilder getPxRankBoolQuery(BoolQueryBuilder boolQueryBuilder, String brand){
        if (org.apache.commons.lang.StringUtils.isNotEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", brand));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }
        return boolQueryBuilder;
    }

    /**
     *
     * @param boolQueryBuilder
     * @param brand
     * @return
     */
    private BoolQueryBuilder getHotSellRankBoolQuery(BoolQueryBuilder boolQueryBuilder, String brand){
        if (org.apache.commons.lang.StringUtils.isNotEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", brand));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }

        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("brand", "other"));
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("brand", "unknown"));

        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("model", "other"));
        return boolQueryBuilder;
    }

    /**
     *
     * @param boolQueryBuilder
     * @param brand
     * @return
     */
    private BoolQueryBuilder getSumByConditionBoolQuery(BoolQueryBuilder boolQueryBuilder, String brand) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", brand));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }
        return boolQueryBuilder;
    }

    /**
     *
     * @param boolQueryBuilder
     * @return
     */
    private BoolQueryBuilder getHoldingRankBoolQuery (BoolQueryBuilder boolQueryBuilder) {

        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("brand", "other"));
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("brand", "unknown"));

        return boolQueryBuilder;
    }

    /**
     *
     * @param boolQueryBuilder
     * @return
     */
    private BoolQueryBuilder getSumByMonthBoolQuery(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        return boolQueryBuilder;
    }

    /**
     *
     * @param personaSummary
     * @param attrField
     * @return
     * @throws Exception
     */
    private String getValueByField(PersonaSummary personaSummary, String attrField) throws Exception{
        Field field = personaSummary.getClass().getDeclaredField(attrField);
        field.setAccessible(true);
        return field.get(personaSummary).toString();

    }

    /**
     *
     * @param personaSummary
     * @param groupByField
     * @param operation
     * @return
     */
    private BoolQueryBuilder generateBoolQuery(PersonaSummary personaSummary, String groupByField, String operation) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.LTL.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").lte(personaSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }

//        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getBrand())) {
//            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", personaSummary.getBrand()));
//        } else {
//            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
//        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }

        String[] fields = {
                "income", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "price",
                "house", "screensize", "car", "married",
                "kids", "gender", "sysver", "brand_name",
                "model"
        };
        for (String field : fields) {
            if (field.equals(groupByField)) {
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, NULLSRT));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "-1"));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "unknown"));
            } else {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NULLSRT));
            }
        }

        return boolQueryBuilder;
    }
    private BoolQueryBuilder generateBoolForUnknownQuery(PersonaSummary personaSummary, String groupByField, String operation) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }


        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand_name", "unknown"));
        String[] fields = {
                "income", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "price",
                "house", "screensize", "car", "married",
                "kids", "gender", "sysver",
                "model","brand"
        };

        for (String field : fields) {
             boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NULLSRT));
        }

        return boolQueryBuilder;
    }
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * task
     */
    class Task implements Callable {

        private BoolQueryBuilder boolQueryBuilder;
        private String attrField;
        private String countField;
        private int size;
        private String key;
        private Map<String, Map<String, String>> resultMap;

        public Task(BoolQueryBuilder boolQueryBuilder, String attrField, String countField, int size, Map<String, Map<String, String>> resultMap, String key) {
            this.boolQueryBuilder = boolQueryBuilder;
            this.attrField = attrField;
            this.countField = countField;
            this.size = size;
            this.resultMap = resultMap;
            this.key = key;
        }

        @Override
        public String call() {
            try {

                Pageable pageable = new PageRequest(0, size);
                SearchQuery searchQuery = new NativeSearchQueryBuilder()
                        .withPageable(pageable)
                        .withQuery(boolQueryBuilder)
                        .build();

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
