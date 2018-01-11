package com.uuzu.mktgo.web;

import com.jthink.spring.boot.starter.hbase.api.HbaseTemplate;
import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.elasticsearch.PersonaSummaryppingRepository;
import com.uuzu.mktgo.pojo.PersonaSummaryHbaseModel;
import com.uuzu.mktgo.service.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhoujin
 */
@Slf4j
@Controller
//@ApiIgnore
@Api(description = "mob_mktgo OpenApi")
@RequestMapping("mktgoEsData")
public class ElasticsearchControl {

    @Autowired
    private PersonaSummaryppingRepository personaSummaryppingRepository;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    ElasticsearchService elasticsearchService;

    @PostMapping("query")
    @ApiOperation(value = "mktgo es 数据查询", notes = "数据准备")
    public ResponseEntity<String> query() {
        try {
            PersonaSummary personaSummary = new PersonaSummary("华为P9","HUAWEI","","cn","","201710");
            Map<String, String> gender = elasticsearchService.getRowKeyByEs(personaSummary, "gender","=");

//            BoolQueryBuilder boolQueryBuilder = QueryBuilders
//                    .boolQuery()
//                    .must(QueryBuilders.matchPhraseQuery("mnt", "201710"))
//                    .must(QueryBuilders.matchPhraseQuery("brand", "HUAWEI"))
//                    .must(QueryBuilders.matchPhraseQuery("model", "华为P9"))
//                    .must(QueryBuilders.matchPhraseQuery("country", "cn"))
//                    .must(QueryBuilders.missingQuery("income"))
//                    .must(QueryBuilders.missingQuery("sysver"))
//                    .must(QueryBuilders.missingQuery("brand_name"))
//                    .must(QueryBuilders.missingQuery("carrier"))
//                    .must(QueryBuilders.missingQuery("segment"))
//                    .must(QueryBuilders.missingQuery("province"))
//                    .must(QueryBuilders.missingQuery("price_range"))
//                    .must(QueryBuilders.missingQuery("agebin"))
//                    .must(QueryBuilders.missingQuery("network"))
//                    .must(QueryBuilders.missingQuery("occupation"))
//                    .must(QueryBuilders.missingQuery("edu"))
//                    .must(QueryBuilders.missingQuery("price"))
//                    .must(QueryBuilders.missingQuery("house"))
//                    .must(QueryBuilders.missingQuery("screensize"))
//                    .must(QueryBuilders.missingQuery("car"))
//                    .must(QueryBuilders.missingQuery("married"))
//                    .must(QueryBuilders.missingQuery("kids"))
//                    .mustNot(QueryBuilders.missingQuery("gender"));

//            SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                    .withQuery(boolQueryBuilder)
//                    .build();
//
//            Page<PersonaSummary> search = personaSummaryppingRepository.search(searchQuery);
//            List<String> result = new ArrayList<>();
//
//            if (search.getTotalElements() > 10) {
//                for (int i = 0; i < 10; i++) {
//                    result.add(search.getContent().get(i).getRow_key());
//                }
//            } else {
//                for (int i = 0; i < search.getTotalElements(); i++) {
//                    result.add(search.getContent().get(i).getRow_key().replace("\u0000","\u0001"));
//                }
//            }
//
//            List<PersonaSummaryHbaseModel> hbaseModels = new ArrayList<>();
//            for (String row_key : result) {
//                PersonaSummaryHbaseModel personaSummaryHbaseModel = hbaseTemplate.get("persona_summary", row_key, (result1, arg1) -> {
//                    if (!result1.isEmpty()) {
//                        PersonaSummaryHbaseModel personaSummaryHbaseModel1 = new PersonaSummaryHbaseModel();
//                        personaSummaryHbaseModel1.setImei_count(Bytes.toString(result1.getValue("cf".getBytes(), "imei_count".getBytes())));
//                        personaSummaryHbaseModel1.setImei_count_incr(Bytes.toString(result1.getValue("cf".getBytes(), "imei_count_incr".getBytes())));
//                        return personaSummaryHbaseModel1;
//                    }
//                    return null;
//                });
//                hbaseModels.add(personaSummaryHbaseModel);
//            }
//
//
////            PersonaSummaryHbaseModel personaSummaryHbaseModel = hbaseTemplate.get("persona_summary", "23=201710\u00010=华为P9\u00011=HUAWEI\u00013=cn\u00016=1", (result1, arg1) -> {
////                if (!result1.isEmpty()) {
////                    PersonaSummaryHbaseModel personaSummaryHbaseModel1 = new PersonaSummaryHbaseModel();
////                    personaSummaryHbaseModel1.setImei_count(Bytes.toString(result1.getValue("cf".getBytes(), "imei_count".getBytes())));
////                    personaSummaryHbaseModel1.setImei_count_incr(Bytes.toString(result1.getValue("cf".getBytes(), "imei_count_incr".getBytes())));
////                    return personaSummaryHbaseModel1;
////                }
////                return null;
////            });
////            hbaseModels.add(personaSummaryHbaseModel);

            return ResponseEntity.ok(gender.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }
}
