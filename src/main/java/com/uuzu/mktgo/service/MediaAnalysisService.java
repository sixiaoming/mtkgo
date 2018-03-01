package com.uuzu.mktgo.service;

import com.jthink.spring.boot.starter.hbase.api.HbaseTemplate;
import com.uuzu.mktgo.elasticsearch.FullAppInfoMonthlySummary;
import com.uuzu.mktgo.elasticsearch.FullAppInfoMonthlySummaryRepository;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.MediaAnalysisModel;
import com.uuzu.mktgo.pojo.OperationEnum;
import com.uuzu.mktgo.util.ArithUtil;
import com.uuzu.mktgo.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
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

import java.util.*;
import java.util.concurrent.*;

/**
 * 媒介分析V2
 * es+hbase架构
 *
 * @author zhoujin
 */
@Service
@Slf4j
public class MediaAnalysisService {
    @Autowired
    DateMonthMapper dateMonthMapper;

    @Autowired
    FullAppInfoMonthlySummaryRepository fullAppInfoMonthlySummaryRepository;

    @Autowired
    HbaseService hbaseService;

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    //媒介分析全量表（选择近六个月时）
//    private static String MEDIAANALYSIS_HBASETABLENAME = "mktgo_full_app_info_monthly_summary_prod";
    private static String MEDIAANALYSIS_HBASETABLENAME = "mktgo_full_app_info_monthly_test_summary_prod";

    //媒介分析增量表（单独选择某一个月份时）
    private static String MEDIAANALYSIS_HBASETABLENAME_INCR = "mktgo_incr_app_info_monthly_test_summary_prod";
//    private static String MEDIAANALYSIS_HBASETABLENAME_INCR = "mktgo_incr_app_info_monthly_summary_prod";


    public List<MediaAnalysisModel> MediaAnalysis(String brand, String model, String price, String country, String province, String date) throws Exception {
        List<MediaAnalysisModel> mediaAnalysisModels = new ArrayList<>();
        //获取最大日期
        String maxMonth = "";
        String hbaseTable = "";
        if (StringUtils.isEmpty(date)) {
            maxMonth = dateMonthMapper.queryMaxMonth();
            hbaseTable = MEDIAANALYSIS_HBASETABLENAME;
        } else {
            maxMonth = date;
            hbaseTable = MEDIAANALYSIS_HBASETABLENAME_INCR;
        }


        FullAppInfoMonthlySummary cnt1AndCnt2Summary = new FullAppInfoMonthlySummary(brand, model, price, country, province, maxMonth);
        FullAppInfoMonthlySummary cnt3AndCnt4Summary = new FullAppInfoMonthlySummary(null, null, null, country, province, maxMonth);

        //获取cnt2 rowkeys
        Map<String, String> cnt2Keys = getRowKeyByEs(cnt1AndCnt2Summary, OperationEnum.EQ.getOperation());
        Map<String, String> cnt2Values = hbaseService.getResultByHbaseTable(cnt2Keys, "cnt2_sum", hbaseTable);
        //size = 1
        Double cnt2Value = 0d;
        for (String key : cnt2Values.keySet()) {
            if (StringUtils.isNotEmpty(cnt2Values.get(key)) && (!cnt2Values.get(key).equals("null"))) {
                cnt2Value = Double.parseDouble(cnt2Values.get(key));
            }
        }

        //获取cnt4 rowkeys
        Map<String, String> cnt4Keys = getRowKeyByEs(cnt3AndCnt4Summary, OperationEnum.EQ.getOperation());
        Map<String, String> cnt4Values = hbaseService.getResultByHbaseTable(cnt4Keys, "cnt4_sum", hbaseTable);
        Double cnt4Value = 0d;
        //size = 1
        for (String key : cnt4Values.keySet()) {
            if (StringUtils.isNotEmpty(cnt4Values.get(key)) && (!cnt4Values.get(key).equals("null"))) {
                cnt4Value = Double.parseDouble(cnt4Values.get(key));
            }
        }

        //获取cnt1 rowkeys
        Map<String, String> cnt1Keys = getRowKeyByEs(cnt1AndCnt2Summary, OperationEnum.BET.getOperation());
        //根据rowkeys获取hbase的相应值
        List<MediaAnalysisModel> cnt1Models = getResultByHbaseTable(cnt1Keys, hbaseTable);

        //获取cnt3 rowkeys

        Map<String, String> cnt3Keys = getRowKeyByEs(cnt3AndCnt4Summary, OperationEnum.BET.getOperation());
        Map<String, String> cnt3Maps = hbaseService.getResultByHbaseTable(cnt3Keys, "cnt3_sum", hbaseTable);


        //select apppkg, icon, name, cate_id, cate_name, cnt1, cnt2, cnt3, cnt4,cnt1/cnt2 as active_rate,(cnt1/cnt2)/(cnt3/cnt4)*100 as index

        /**
         * 排除null值 by zhongqi
         */
        for (MediaAnalysisModel mediaAnalysisModel : cnt1Models) {
            if(mediaAnalysisModel != null){

                Double cnt3 = Double.parseDouble(StringUtils.isEmpty(cnt3Maps.get(mediaAnalysisModel.getApppkg())) ? "0" : cnt3Maps.get(mediaAnalysisModel.getApppkg()));
                Double activeRate = 0d;
                try {
                    activeRate = ArithUtil.div(Double.parseDouble(mediaAnalysisModel.getCnt1()), cnt2Value);
                } catch (Exception e) {
                    log.error(mediaAnalysisModel.toString());
                }
                mediaAnalysisModel.setActive_rate(activeRate);
                Double index = 0d;
                try {
                    index = ArithUtil.div(activeRate, ArithUtil.div(cnt3, cnt4Value)) * 100;
                } catch (Exception e) {
                    log.error(mediaAnalysisModel.toString());
                }
                mediaAnalysisModel.setIndex(index);
                mediaAnalysisModel.setCnt2(cnt2Value.toString());
                mediaAnalysisModel.setCnt3(cnt3.toString());
                mediaAnalysisModel.setCnt4(cnt4Value.toString());
            }
        }
        //比较前去除null元素
        cnt1Models.removeAll(Collections.singleton(null));
        Collections.sort(cnt1Models, new Comparator<MediaAnalysisModel>() {
            @Override
            public int compare(MediaAnalysisModel o1, MediaAnalysisModel o2) {
                if ( o1.getActive_rate() < o2.getActive_rate()) {
                    return 1;
                } else if ( o1.getActive_rate() == (o2.getActive_rate())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return cnt1Models;
    }

    /**
     * 多任务处理器
     */
    class Task implements Callable {

        int tryNum = 1;

        private String sql;

        public Task(String sql) {
            this.sql = sql;
        }

        @Override
        public String call() {
            try {
                String genderData = HttpUtil.sendHttpClient(sql);

                return genderData;
            } catch (Exception e) {
                log.error((String.format("Execute SQL is %s:failed[%d] : %s", sql, tryNum, e.getMessage())));
            }
            return null;

        }
    }

    private static String NULLSRT = "NIL";


    /**
     * es查询
     *
     * @param fullAppInfoMonthlySummary
     * @param operation
     * @return
     * @throws Exception
     */
    public Map<String, String> getRowKeyByEs(FullAppInfoMonthlySummary fullAppInfoMonthlySummary, String operation) throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mnt", fullAppInfoMonthlySummary.getMnt()));
        if (StringUtils.isNotEmpty(fullAppInfoMonthlySummary.getPrice_range())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", fullAppInfoMonthlySummary.getPrice_range()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("price_range", NULLSRT));
        }
        if (StringUtils.isNotEmpty(fullAppInfoMonthlySummary.getCountry())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", fullAppInfoMonthlySummary.getCountry()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("country", NULLSRT));
        }
        if (StringUtils.isNotEmpty(fullAppInfoMonthlySummary.getProvince())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", fullAppInfoMonthlySummary.getProvince()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province", NULLSRT));
        }

        //cnt3和cnt4条件为品牌和机型为null
        if (StringUtils.isNotEmpty(fullAppInfoMonthlySummary.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", fullAppInfoMonthlySummary.getBrand()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", NULLSRT));
        }
        if (StringUtils.isNotEmpty(fullAppInfoMonthlySummary.getModel())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", fullAppInfoMonthlySummary.getModel()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("model", NULLSRT));
        }

        //cnt1,cnt3为apppkg<>'NIL'; cnt2,cnt4为apppkg='NIL'
        if (OperationEnum.EQ.getOperation().equals(operation)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("apppkg", NULLSRT));
        } else if (OperationEnum.BET.getOperation().equals(operation)) {
            boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("apppkg", NULLSRT));
        }


        Pageable pageable = new PageRequest(0, 10000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();

        Page<FullAppInfoMonthlySummary> search = fullAppInfoMonthlySummaryRepository.search(searchQuery);
        Map<String, String> result = new HashMap<>();
        for (FullAppInfoMonthlySummary summary : search) {
            result.put(summary.getApppkg(), summary.getRow_key().replace("\u0000", "\u0001"));
        }

        return result;

    }


    /**
     * hbase查询
     *
     * @param rowkeys
     * @param tableName
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<MediaAnalysisModel> getResultByHbaseTable(Map<String, String> rowkeys, String tableName) throws InterruptedException, ExecutionException {
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);

        for (String fieldkey : rowkeys.keySet()) {
            completionService.submit(new HbaseTask(rowkeys.get(fieldkey), fieldkey, tableName));
        }
        List<MediaAnalysisModel> results = new ArrayList<>();
        for (int i = 0; i < rowkeys.size(); i++) {
            Future<MediaAnalysisModel> future = completionService.take();
            results.add(future.get());
        }

        return results;
    }


    class HbaseTask implements Callable {

        private String value;
        private String tableName;
        private String apppkg;

        public HbaseTask(String value, String apppkg, String tableName) {
            this.value = value;
            this.tableName = tableName;
            this.apppkg = apppkg;
        }

        @Override
        public MediaAnalysisModel call() {
            try {
                MediaAnalysisModel mediaAnalysisModel = hbaseTemplate.get(tableName, value, (result1, arg1) -> {
                    if (!result1.isEmpty()) {
                        MediaAnalysisModel mediaAnalysisModel1 = new MediaAnalysisModel();
                        mediaAnalysisModel1.setCate_id(Bytes.toString(result1.getValue("cf".getBytes(), "cate_id_max".getBytes())));
                        mediaAnalysisModel1.setCate_name(Bytes.toString(result1.getValue("cf".getBytes(), "cate_name_max".getBytes())));
                        mediaAnalysisModel1.setCnt1(Bytes.toString(result1.getValue("cf".getBytes(), "cnt1_sum".getBytes())));
                        mediaAnalysisModel1.setCnt2(Bytes.toString(result1.getValue("cf".getBytes(), "cnt2_sum".getBytes())));
                        mediaAnalysisModel1.setCnt3(Bytes.toString(result1.getValue("cf".getBytes(), "cnt3_sum".getBytes())));
                        mediaAnalysisModel1.setCnt4(Bytes.toString(result1.getValue("cf".getBytes(), "cnt4_sum".getBytes())));
                        mediaAnalysisModel1.setIcon(Bytes.toString(result1.getValue("cf".getBytes(), "icon_max".getBytes())));
                        mediaAnalysisModel1.setName(Bytes.toString(result1.getValue("cf".getBytes(), "name_max".getBytes())));

                        //20180228 chenwei change the hbase table
                        mediaAnalysisModel1.setCate_l2_id(Bytes.toString(result1.getValue("cf".getBytes(), "cate_l2_id".getBytes())));
                        mediaAnalysisModel1.setCate_l2(Bytes.toString(result1.getValue("cf".getBytes(), "cate_l2".getBytes())));

                        mediaAnalysisModel1.setApppkg(apppkg);
                        return mediaAnalysisModel1;
                    }
                    return null;
                });
                return mediaAnalysisModel;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }


}
