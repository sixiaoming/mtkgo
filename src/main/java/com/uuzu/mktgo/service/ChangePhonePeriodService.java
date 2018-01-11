package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.CycleSummary;
import com.uuzu.mktgo.elasticsearch.CycleSummaryRepository;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.ChangePhonePeriodModel;
import com.uuzu.mktgo.pojo.OperationEnum;
import com.uuzu.mktgo.util.DateUtil;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shieh on 2017/10/19.
 */
@Service
@Slf4j
public class ChangePhonePeriodService {

    private static final String HBASE_TABLE = "conversation_cycle_summary_prod";
    private static final String NIL_STR = "NIL";

    @Autowired
    private CycleSummaryRepository cycleSummaryRepository;
    @Autowired
    private HbaseService hbaseService;
    @Autowired
    DateMonthMapper dateMonthMapper;


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
    public ChangePhonePeriodModel changePhonePeriod(String brand, String model, String price, String country,
                                                    String province) throws Exception{
        ChangePhonePeriodModel changePhonePeriodModel = new ChangePhonePeriodModel();
        String mnt = dateMonthMapper.queryMaxMonth();

        String month = DateUtil.getLastMonthDate(mnt, 24);

        CycleSummary cycleSummary = new CycleSummary(model, country, brand, province, mnt, price, month);

        Map<String, String> sumRowKeys = getRowKeysByEs(cycleSummary, OperationEnum.GTL.getOperation());
        Map<String, String> changeTimesCountMap = hbaseService.getResultByHbaseTable(sumRowKeys, "change_times_count", HBASE_TABLE);
        Map<String, String> durationDaysMap = hbaseService.getResultByHbaseTable(sumRowKeys, "duration_days", HBASE_TABLE);
        convertChangePhonePeriodModel(changePhonePeriodModel, convertToQuarterMap(changeTimesCountMap), convertToQuarterMap(durationDaysMap));
        return changePhonePeriodModel;

    }


    /**
     *
     * @param changePhonePeriodModel
     * @param changeTimeCountMap
     * @param durationDaysMap
     */
    private void convertChangePhonePeriodModel(ChangePhonePeriodModel changePhonePeriodModel,
                                               Map<String, Long> changeTimeCountMap, Map<String, Long> durationDaysMap) {

        List<BaseModel> baseModels = new ArrayList<>();
        for (String key : changeTimeCountMap.keySet()) {
            baseModels.add(new BaseModel(key, new BigDecimal((float) durationDaysMap.get(key) / changeTimeCountMap.get(key) / 30).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()));
        }

        // return latest data
        if (baseModels.size() > 8) {
            baseModels = baseModels.subList(baseModels.size() - 8, baseModels.size());
        }

        changePhonePeriodModel.setPeriod(baseModels);
    }


    /**
     *
     * @param map
     * @return
     */
    private Map<String, Long> convertToQuarterMap(Map<String, String> map) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Map<String, Integer> result = new HashMap<>();
        Map<String, Long> sumResult = new HashMap<>();
        for (String key: map.keySet()) {
            String changeTimeCount = map.get(key);
            try {
                Date date = sdf.parse(key);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int quarter = (month + 2) / 3;
                if (result.get(year + "Q" + quarter) == null) {
                    result.put(year + "Q" + quarter, 1);
                    sumResult.put(year + "Q" + quarter, Long.valueOf(changeTimeCount));
                } else {
                    result.put(year + "Q" + quarter, result.get(year + "Q" + quarter) + 1);
                    sumResult.put(year + "Q" + quarter, sumResult.get(year + "Q" + quarter) + Long.valueOf(changeTimeCount));
                }
            } catch (Exception e) {
                continue;
            }

        }

        Map<String, Long> returnResult = new TreeMap<>(( o1, o2)-> o2.compareTo(o1));
        for (String s : result.keySet()) {
            if (result.get(s) == 3 && sumResult.get(s) != null) {
                returnResult.put(s, sumResult.get(s));
            }
        }

        return returnResult;
    }

    /**
     *
     * @param cycleSummary
     * @param operation
     * @return
     */
    private Map<String, String> getRowKeysByEs(CycleSummary cycleSummary, String operation) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(cycleSummary.getMonth()) && OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("month", cycleSummary.getMonth()));
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("month", NIL_STR));
        } else if (StringUtils.isNotEmpty(cycleSummary.getMonth()) && OperationEnum.GTL.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("month").gte(cycleSummary.getMonth()));
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("month", NIL_STR));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("month", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getMnt())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", cycleSummary.getMnt()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", cycleSummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", cycleSummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", cycleSummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", cycleSummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NIL_STR));
        }

        if (StringUtils.isNotEmpty(cycleSummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", cycleSummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NIL_STR));
        }

        Pageable pageable = new PageRequest(0, 1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();

        Page<CycleSummary> search = cycleSummaryRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (CycleSummary summary : search) {
            result.put(summary.getMonth(), summary.getRow_key().replace("\u0000", "\u0001"));
        }

        return result;
    }


}
