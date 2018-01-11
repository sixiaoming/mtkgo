package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.elasticsearch.PersonaSummaryFinalppingRepository;
import com.uuzu.mktgo.elasticsearch.PersonaSummary_final;
import com.uuzu.mktgo.elasticsearch.PersonaSummaryppingRepository;
import com.uuzu.mktgo.pojo.OperationEnum;
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
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongqi
 */
@Service
@Slf4j
public class OverviewESService {

    @Autowired
    private PersonaSummaryppingRepository      personaSummaryppingRepository;
    @Autowired
    private PersonaSummaryFinalppingRepository personaSummaryppingRepositoryFinal;
    private static String NULLSRT = "NIL";

    /**
     * 首页概览接口es查询
     *,String sortField,int pageSize
     * @param
     * @return
     */

    public  Page<PersonaSummary> getRowKeyByEs(PersonaSummary personaSummary, String countfield, String operation) throws Exception {
        BoolQueryBuilder  boolQueryBuilder = handleBoolQueryBuilder(personaSummary,countfield,operation);
        Pageable pageable = new PageRequest(0, 10000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
        return search;
    }

    public  Page<PersonaSummary> getRowKeyByEsForHotselling(PersonaSummary personaSummary, String countfield, String operation) throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("\\|")[0]).lte(personaSummary.getMnt().split("\\|")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }

        String[] fields = {"income","model","brand", "sysver", "brand_name", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "house", "screensize", "car", "married", "kids", "gender"};
        boolQueryBuilder = boolQueryBuilderFilter(fields,boolQueryBuilder,countfield);
        //单独处理价格
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("price", NULLSRT));
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("price", "-1"));
        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("price", "unknown"));
        Pageable pageable = new PageRequest(0, 10000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return search;
    }

    public  Map<String, String> getRowKeyFinal(PersonaSummary_final personaSummary, String countfield, String operation) throws Exception {

         String NULLSRT = "NIL";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("\\|")[0]).lte(personaSummary.getMnt().split("\\|")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }

        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }

        Pageable pageable = new PageRequest(0, 10000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        Page<PersonaSummary_final> search = personaSummaryppingRepositoryFinal.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary_final summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return result;
    }


    public  Map<String, String> getRowKeyByEsForModel(PersonaSummary personaSummary, String countfield, String operation) throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("\\|")[0]).lte(personaSummary.getMnt().split("\\|")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", personaSummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NULLSRT));
        }

        String[] fields = {"income","brand", "sysver", "brand_name", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "house", "screensize", "car", "married", "kids", "gender","price"};
        boolQueryBuilder = boolQueryBuilderFilter(fields,boolQueryBuilder,countfield);
        Pageable pageable = new PageRequest(0, 1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return result;
    }

    public  Map<String, String> getRowKeyByEsForUnknown(PersonaSummary personaSummary, String countfield, String operation) throws Exception {


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("\\|")[0]).lte(personaSummary.getMnt().split("\\|")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }


        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", "unknown"));

        String[] fields = {"income","brand", "sysver", "brand_name", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "house", "screensize", "car", "married", "kids", "gender","price"};
        for (String field : fields) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NULLSRT));
        }
        boolQueryBuilder = boolQueryBuilderFilter(fields,boolQueryBuilder,countfield);
        Pageable pageable = new PageRequest(0, 1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return result;
    }

    public BoolQueryBuilder handleBoolQueryBuilder(PersonaSummary personaSummary, String countfield,String operation){
        String NULLSRT = "NIL";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("\\|")[0]).lte(personaSummary.getMnt().split("\\|")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }

        if (StringUtils.isNotEmpty(personaSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", personaSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", personaSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }
        if (StringUtils.isNotEmpty(personaSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", personaSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }
        String[] fields = {"income","model","brand" ,"sysver", "brand_name", "carrier", "segment", "agebin",
                "network", "occupation", "edu", "price", "house", "screensize", "car", "married", "kids", "gender"};
        if(countfield.length()>0 && countfield!= null){
            return boolQueryBuilderFilter(fields,boolQueryBuilder,countfield);
        }else{
            return boolQueryBuilder;
        }


    }

    public BoolQueryBuilder boolQueryBuilderFilter(String[] fields ,BoolQueryBuilder boolQueryBuilder,String countfield){

        for (String field : fields) {
            if (field.equals(countfield)) {
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, NULLSRT));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "-1"));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "unknown"));
            } else {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, NULLSRT));
            }
        }
        return boolQueryBuilder;
    }
    public String getValueByField(PersonaSummary personaSummary, String countfield) throws Exception {
        Field field = personaSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(personaSummary).toString();
    }
    public String getValueByField(PersonaSummary_final personaSummary, String countfield) throws Exception {
        Field field = personaSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(personaSummary).toString();
    }
}
