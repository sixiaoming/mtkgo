package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.BrandFansSummary;
import com.uuzu.mktgo.elasticsearch.BrandFansSummaryRepository;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.*;
import lombok.extern.slf4j.Slf4j;
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

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * Created by shieh on 2017/10/18.
 */
@Service
@Slf4j
public class FansPortraitService {

    private static String NULLSRT = "NIL";
    private static final String HBASE_TABLE = "brand_fans_summary_prod";
    @Autowired
    private HbaseService hbaseService;
    @Autowired
    DateMonthMapper dateMonthMapper;
    @Autowired
    DictInfoService dictInfoService;

    @Autowired
    BrandFansSummaryRepository brandFansSummaryRepository;


    /**
     *
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param province
     * @return
     * @throws Exception
     */
    public FansPortraitModel fansPortrait(String brand, String model, String price, String country,
                                           String province) throws Exception{
        FansPortraitModel fansPortraitModel = new FansPortraitModel();

        //获取最大日期
        String maxMonth = dateMonthMapper.queryMaxMonth();

        //条件
        BrandFansSummary brandFansSummary = new BrandFansSummary(country, brand, province, maxMonth);

        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);

        completionService.submit(new Task(brandFansSummary, "gender", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "agebin", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "income", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "segment", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "edu", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "kids", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "car", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "house", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "network_new", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "married", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "occupation", "fans_count", fansPortraitModel));
        completionService.submit(new Task(brandFansSummary, "carrier_new", "fans_count", fansPortraitModel));

        for (int i = 0; i < 12; i++) {
            Future<String> future = completionService.take();
//            log.info("thread======:" + future.get());
        }

        return fansPortraitModel;

    }


    /**
     *
     * @param fansPortraitModel
     * @param map
     * @param countfield
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private FansPortraitModel convertModelByFieldAndMap(FansPortraitModel fansPortraitModel, Map<String, String> map, String countfield) throws NoSuchFieldException, IllegalAccessException {
        //获取所有的字典
        Map<String, Map<String, String>> dictInfo = dictInfoService.getDictInfo();


        long sum = 0l;
        //sum
        for (String key : map.keySet()) {
            if (StringUtils.equals("other", key)) continue;
            sum += Long.parseLong(map.get(key));
        }

        //计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (String key : map.keySet()) {
            if (StringUtils.equals("other", key)) continue;
            if ("carrier_new".equals(countfield)) {
                baseModels.add(new BaseModel(key, Double.parseDouble(map.get(key)) / sum));
            } else if ("network_new".equals(countfield)) {
                baseModels.add(new BaseModel(dictInfo.get("network").get(key), Double.parseDouble(map.get(key)) / sum));
            } else {
                baseModels.add(new BaseModel(dictInfo.get(countfield).get(key), Double.parseDouble(map.get(key)) / sum));
            }
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

        if ("carrier_new".equals(countfield) && baseModels.size() > 10) {
            baseModels = baseModels.subList(0, 10);
        }


        //通过字段反射设置对象的值
        if (map != null) {
            Field field = fansPortraitModel.getClass().getDeclaredField(countfield);
            field.setAccessible(true);
            field.set(fansPortraitModel, baseModels);
        }
        return fansPortraitModel;
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * fansPortrait task
     */
    class Task implements Callable {

        private BrandFansSummary brandFansSummary;
        private String countfield;
        private String field;
        private FansPortraitModel fansPortraitModel;

        public Task(BrandFansSummary brandFansSummary, String countfield, String field, FansPortraitModel fansPortraitModel) {
            this.brandFansSummary = brandFansSummary;
            this.countfield = countfield;
            this.field = field;
            this.fansPortraitModel = fansPortraitModel;
        }

        @Override
        public String call() {
            try {
                //先从es中获取相应的rowkey
                Map<String, String> esRowKeys = getRowKeyByEs(brandFansSummary, countfield, OperationEnum.EQ.getOperation());
                //将相应的rowkey从hbase中获取对应的结果
                Map<String, String> hbaseResult = hbaseService.getResultByHbaseTable(esRowKeys, field, HBASE_TABLE);
                //将结果转换并计算百分比
                convertModelByFieldAndMap(fansPortraitModel, hbaseResult, countfield);

                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;

        }
    }


    /**
     *
     * @param brandFansSummary
     * @param countfield
     * @param operation
     * @return
     * @throws Exception
     */
    private Map<String, String> getRowKeyByEs(BrandFansSummary brandFansSummary, String countfield, String operation) throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(brandFansSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", brandFansSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(brandFansSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(brandFansSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }

        if (StringUtils.isNotEmpty(brandFansSummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", brandFansSummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }

        if (StringUtils.isNotEmpty(brandFansSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", brandFansSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }

        if (StringUtils.isNotEmpty(brandFansSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", brandFansSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }

        String[] fields = {"income", "carrier_new", "segment", "agebin", "network_new", "occupation", "edu", "house", "car", "married", "kids", "gender"};
        for (String field : fields) {
            if (field.equals(countfield)) {
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, NULLSRT));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "-1"));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "unknown"));
            } else {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NULLSRT));
            }
        }

        Pageable pageable = new PageRequest(0, 1000);
        //SortBuilder sortBuilder  = SortBuilders.fieldSort(sortField).order(SortOrder.ASC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                //.withSort(sortBuilder)
                .withQuery(boolQueryBuilder)
                .build();

        Page<BrandFansSummary> search = brandFansSummaryRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (BrandFansSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }

        return result;
    }

    /**
     *
     * @param brandFansSummary
     * @param countfield
     * @return
     * @throws Exception
     */
    private String getValueByField(BrandFansSummary brandFansSummary, String countfield) throws Exception {
        Field field = brandFansSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(brandFansSummary).toString();
    }

}
