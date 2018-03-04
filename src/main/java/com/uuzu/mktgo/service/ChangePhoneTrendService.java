package com.uuzu.mktgo.service;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.elasticsearch.*;
import com.uuzu.mktgo.mapper.BrandModelMappingNewMapper;
import com.uuzu.mktgo.mapper.BrandPicMapper;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.*;
import com.uuzu.mktgo.util.AssembleUtil;

/**
 * Created by shieh on 2017/10/19.
 */
@Service
public class ChangePhoneTrendService {

    @Autowired
    private BrandModelMappingNewMapper         brandModelMappingNewMapper;

    @Autowired
    private BrandPicMapper                     brandPicMapper;

    private static final String                HBASE_TABLE_SHRINK       = "conversation_source_prod";
    private static final String                HBASE_TABLE_SHRINK_TREND = "conversation_dest_prod";

    private static final String                NULLSRT                  = "NIL";

    @Autowired
    private HbaseService                       hbaseService;
    @Autowired
    DateMonthMapper                            dateMonthMapper;

    @Autowired
    ConversationPersonaSummaryRepository       conversationPersonaSummaryRepository;

    @Autowired
    ConversationPersonaSummaryShrinkRepository conversationPersonaSummaryShrinkRepository;

    @Autowired
    ConPersonaSummaryShrinkTrendRepository     conPersonaSummaryShrinkTrendRepository;

    @Autowired
    DictInfoService                            dictInfoService;

    public ChangePhoneTrendModel changePhoneTrend(String brand, String model, String price, String country, String province) throws Exception {
        ChangePhoneTrendModel changePhoneTrendModel = new ChangePhoneTrendModel();
        SourcePortraitModel sourcePortraitModel = new SourcePortraitModel();
        String maxMonth = dateMonthMapper.queryMaxMonth();

        ConversationPersonaSummaryShrink sumSourceModel = new ConversationPersonaSummaryShrink(brand, country, province, maxMonth, price, model);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", StringUtils.isEmpty(sumSourceModel.getMnt()) ? NULLSRT : sumSourceModel.getMnt()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model_new", StringUtils.isEmpty(sumSourceModel.getModel_new()) ? NULLSRT : sumSourceModel.getModel_new()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand_new", StringUtils.isEmpty(sumSourceModel.getBrand_new()) ? NULLSRT : sumSourceModel.getBrand_new()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", StringUtils.isEmpty(sumSourceModel.getCountry()) ? NULLSRT : sumSourceModel.getCountry()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", StringUtils.isEmpty(sumSourceModel.getProvince()) ? NULLSRT : sumSourceModel.getProvince()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", StringUtils.isEmpty(sumSourceModel.getPrice_range()) ? NULLSRT : sumSourceModel.getPrice_range()));

        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("car", NULLSRT));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("married", NULLSRT));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("house", NULLSRT));

        Pageable pageable = new PageRequest(0, 1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(boolQueryBuilder).build();

        Page<ConversationPersonaSummaryShrink> search = conversationPersonaSummaryShrinkRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        result.put("source", search.getContent().get(0).getRow_key().replace("\u0000", "\u0001"));
        String[] attributes = new String[] { "agebin", "gender", "income", "segment", "edu", "kids", "car", "house", "network", "married", "occupation", "carrier" };
        for (String attribute : attributes) {
            Map<String, String> hbaseResult = hbaseService.getResultByHbaseTable(result, attribute, HBASE_TABLE_SHRINK);
            convertBaseData(hbaseResult, "source", attribute, sourcePortraitModel);
        }

        String field = "brand";
        if (!StringUtils.isEmpty(model)) {
            field = "model";
        }
        Map<String, String> fieldResult = hbaseService.getResultByHbaseTable(result, field, HBASE_TABLE_SHRINK);
        convertPhoneData(fieldResult, "source", "sourceModel", sourcePortraitModel);

        /*
         * source end
         */

        /*
         * trend begin
         */
        TrendPortraitModel trendPortraitModel = new TrendPortraitModel();
        ConversationPersonaSummaryShrinkTrend trendModel = new ConversationPersonaSummaryShrinkTrend(brand, country, province, maxMonth, price, model);
        BoolQueryBuilder trendBoolQueryBuilder = QueryBuilders.boolQuery();

        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", StringUtils.isEmpty(trendModel.getMnt()) ? NULLSRT : trendModel.getMnt()));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", StringUtils.isEmpty(trendModel.getModel()) ? NULLSRT : trendModel.getModel()));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", StringUtils.isEmpty(trendModel.getBrand()) ? NULLSRT : trendModel.getBrand()));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", StringUtils.isEmpty(trendModel.getCountry()) ? NULLSRT : trendModel.getCountry()));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", StringUtils.isEmpty(trendModel.getProvince()) ? NULLSRT : trendModel.getProvince()));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", StringUtils.isEmpty(trendModel.getPrice_range()) ? NULLSRT : trendModel.getPrice_range()));

        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("car", NULLSRT));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("married", NULLSRT));
        trendBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("house", NULLSRT));

        Pageable trendPageable = new PageRequest(0, 1000);
        SearchQuery trendSearchQuery = new NativeSearchQueryBuilder().withPageable(trendPageable).withQuery(trendBoolQueryBuilder).build();

        Page<ConversationPersonaSummaryShrinkTrend> trendSearch = conPersonaSummaryShrinkTrendRepository.search(trendSearchQuery);
        Map<String, String> trendResult = new HashMap<>();
        trendResult.put("trend", trendSearch.getContent().get(0).getRow_key().replace("\u0000", "\u0001"));

        for (String attribute : attributes) {
            Map<String, String> hbaseResult = hbaseService.getResultByHbaseTable(trendResult, attribute, HBASE_TABLE_SHRINK_TREND);
            convertBaseData(hbaseResult, "trend", attribute, trendPortraitModel);
        }

        String trendField = "brand_new";
        if (!StringUtils.isEmpty(model)) {
            trendField = "model_new";
        }
        Map<String, String> trendFieldResult = hbaseService.getResultByHbaseTable(trendResult, trendField, HBASE_TABLE_SHRINK_TREND);
        convertPhoneData(trendFieldResult, "trend", "trendModel", trendPortraitModel);

        /*
         * trend end
         */

        if (StringUtils.isEmpty(model)) {
            String picName = brandPicMapper.queryPicByBrand(brand);
            final String picUrl = "http://oy0hr93xz.bkt.clouddn.com/";
            changePhoneTrendModel.setModelInfo(new TrendModel(brand, null, picName == null ? null : new ArrayList<String>() {

                {
                    add(picUrl + picName);
                }
            }, null, null));
        } else {
            BrandModelMappingNewModel bmmnm = brandModelMappingNewMapper.queryModelInfoByModel(model);
            changePhoneTrendModel.setModelInfo(bmmnm == null ? new TrendModel(null, null, null, null, null) : new TrendModel(bmmnm.getClean_brand(), bmmnm.getClean_model(),
                                                                                                                             AssembleUtil.splitBySymbol(bmmnm.getModel_pic_address(), ";"), bmmnm.getPrice(),
                                                                                                                             bmmnm.getPublic_time()));
        }
        changePhoneTrendModel.setTrend(trendPortraitModel);
        changePhoneTrendModel.setSource(sourcePortraitModel);
        return changePhoneTrendModel;
    }

    private <T> void convertPhoneData(Map<String, String> hbaseResult, String hbaseKey, String attributeKey, T t) throws Exception {
        String hbaseValue = hbaseResult.get(hbaseKey);
        if (StringUtils.isBlank(hbaseValue) || StringUtils.equals(hbaseValue, "null")) return;
        String keys = hbaseResult.get(hbaseKey).split("\u0003")[0];
        String values = hbaseResult.get(hbaseKey).split("\u0003")[1];
        String key[] = keys.split("\u0002");
        String value[] = values.split("\u0002");
        long sum = 0l;
        // sum
        for (int i = 0; i < key.length; i++) {
            if (org.apache.commons.lang.StringUtils.equals("-1", key[i]) || org.apache.commons.lang.StringUtils.equals("unknown", key[i]) || org.apache.commons.lang.StringUtils.equals("other", key[i])) continue;
            sum += Long.parseLong(value[i]);
        }

        // 计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (int i = 0; i < key.length; i++) {
            // filter key is -1 or unknown
            if (org.apache.commons.lang.StringUtils.equals("-1", key[i]) || org.apache.commons.lang.StringUtils.equals("unknown", key[i]) || org.apache.commons.lang.StringUtils.equals("other", key[i])) continue;
            baseModels.add(new BaseModel(key[i], Double.parseDouble(value[i]) / sum));
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

        if (baseModels.size() > 10) {
            baseModels = baseModels.subList(0, 10);
        }

        Field field = t.getClass().getDeclaredField(attributeKey);
        field.setAccessible(true);
        field.set(t, baseModels);

    }

    private <T> void convertBaseData(Map<String, String> hbaseResult, String hbaseKey, String attributeKey, T t) throws Exception {
        String hbaseValue = hbaseResult.get(hbaseKey);
        if (StringUtils.isBlank(hbaseValue) || StringUtils.equals(hbaseValue, "null")) return;
        String keys = hbaseResult.get(hbaseKey).split("\u0003")[0];
        String values = hbaseResult.get(hbaseKey).split("\u0003")[1];
        String key[] = keys.split("\u0002");
        String value[] = values.split("\u0002");
        // 获取所有的字典
        Map<String, Map<String, String>> dictInfo = dictInfoService.getDictInfo();
        long sum = 0l;
        // sum
        for (int i = 0; i < key.length; i++) {
            if (org.apache.commons.lang.StringUtils.equals("-1", key[i]) || org.apache.commons.lang.StringUtils.equals("unknown", key[i]) || org.apache.commons.lang.StringUtils.equals("other", key[i])) continue;
            sum += Long.parseLong(value[i]);
        }

        // 计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (int i = 0; i < key.length; i++) {
            // filter key is -1 or unknown
            if (org.apache.commons.lang.StringUtils.equals("-1", key[i]) || org.apache.commons.lang.StringUtils.equals("unknown", key[i]) || org.apache.commons.lang.StringUtils.equals("other", key[i])) continue;
            if ("carrier".equals(attributeKey)) {
                baseModels.add(new BaseModel(key[i], Double.parseDouble(value[i]) / sum));
            } else {
                baseModels.add(new BaseModel(dictInfo.get(attributeKey).get(key[i]), Double.parseDouble(value[i]) / sum));
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

        if ("carrier".equals(attributeKey)) {
            if (baseModels.size() > 10) {
                baseModels = baseModels.subList(0, 10);
            }
        }

        Field field = t.getClass().getDeclaredField(attributeKey);
        field.setAccessible(true);
        field.set(t, baseModels);
    }
}
