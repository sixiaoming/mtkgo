package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
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
 * @author zhoujin
 */
@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    private PersonaSummaryppingRepository personaSummaryppingRepository;

    private static String NULLSRT = "NIL";

    /**
     * 标准画像接口es查询
     *,String sortField,int pageSize
     * @param personaSummary
     * @return
     */
    //(personaSummary,gender, <)
    public Map<String, String> getRowKeyByEs(PersonaSummary personaSummary, String countfield, String operation) throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt()));
        } else if (StringUtils.isNotEmpty(personaSummary.getMnt()) && OperationEnum.BET.getOperation().equals(operation)){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("mnt").gte(personaSummary.getMnt().split("|")[0]).lte(personaSummary.getMnt().split("")[1]));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NULLSRT));
        }

        if (StringUtils.isNotEmpty(personaSummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", personaSummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NULLSRT));
        }

        if (StringUtils.isNotEmpty(personaSummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", personaSummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
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

        String[] fields = {"income", "sysver", "brand_name", "carrier", "segment", "price_range", "agebin",
                "network", "occupation", "edu", "price", "house", "screensize", "car", "married", "kids", "gender"};
        for (String field : fields) {
            if (field.equals(countfield)) {
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, NULLSRT));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "-1"));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "unknown"));
                boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(field, "other"));
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

        Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }

        return result;
    }

    public String getValueByField(PersonaSummary personaSummary, String countfield) throws Exception {
        Field field = personaSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(personaSummary).toString();
    }








}
