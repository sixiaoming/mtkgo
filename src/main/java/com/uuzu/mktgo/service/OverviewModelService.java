package com.uuzu.mktgo.service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.mapper.BrandModelMappingNewMapper;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.mapper.DictMapper;
import com.uuzu.mktgo.pojo.*;
import com.uuzu.mktgo.util.DateUtil;

/**
 * @author zhongqi
 */
@Service
@Slf4j
public class OverviewModelService {

    @Autowired
    OverviewAllService             overviewAllService;
    @Autowired
    private OverviewESService      overviewESService;
    @Autowired
    private HbaseService           hbaseService;
    @Autowired
    DateMonthMapper                dateMonthMapper;
    @Autowired
    DictInfoService                dictInfoService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private DictMapper             dictMapper;
    @Autowired
    BrandModelMappingNewMapper     brandModelMappingNewMapper;

    /**
     * 机型概览 V2.0 es+hbase架构
     *
     * @param price
     * @param country
     * @param province
     * @param date
     * @return
     * @throws Exception
     */
    public ModelOverviewModel overview_model(String model, String price, String country, String province, String date) throws Exception {
        ModelOverviewModel mom = new ModelOverviewModel();
        BrandModelMappingNewModel brandModelMappingNewModel = brandModelMappingNewMapper.queryModelInfoByModel(model);
        mom.setPrice(brandModelMappingNewModel.getPrice());
        mom.setUptime(brandModelMappingNewModel.getPublic_time());
        mom.setPics(Arrays.asList(brandModelMappingNewModel.getModel_pic_address().split(";")));
        mom.setModel(model);
        String endDate = null;
        String imei = null;
        if (StringUtils.isBlank(date)) {
            endDate = dateMonthMapper.queryMaxMonth();
            imei = "imei_count";
        } else {
            endDate = date;
            imei = "imei_count_incr";
        }
        String startDate = DateUtil.getLastSixMonthDate(endDate);
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);
        PersonaSummary personaSummary = new PersonaSummary(model, "", price, country, province, startDate + "|" + endDate);
        PersonaSummary personaSummary2 = new PersonaSummary(model, "", price, country, province, endDate);
        completionService.submit(new OverviewModelService.Task(personaSummary, "mnt", imei, "brand", mom));
        completionService.submit(new OverviewModelService.Task(personaSummary2, "gender", imei, "gender", mom));
        completionService.submit(new OverviewModelService.Task(personaSummary2, "agebin", imei, "age", mom));
        for (int i = 0; i < 3; i++) {
            Future<String> future = completionService.take();
        }
        return mom;
    }

    class Task implements Callable {

        private PersonaSummary     personaSummary;
        private String             countfield;
        private String             field;
        private String             type;
        private ModelOverviewModel mom;

        public Task(PersonaSummary personaSummary, String countfield, String field, String type, ModelOverviewModel mom) {
            this.personaSummary = personaSummary;
            this.countfield = countfield;
            this.field = field;
            this.type = type;
            this.mom = mom;
        }

        @Override
        public String call() {
            try {
                if (type.equals("brand")) {
                    this.marketShareTrend(personaSummary, countfield, field);
                } else if (type.equals("gender")) {
                    this.gendetProportion(personaSummary, countfield, field);
                } else if (type.equals("age")) {
                    this.ageProportion(personaSummary, countfield, field);
                }
                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }

        public void ageProportion(PersonaSummary personaSummary, String countfield, String field) throws Exception {
            Map<String, String> age_search_up = overviewESService.getRowKeyByEsForModel(personaSummary, countfield, OperationEnum.EQ.getOperation());
            Map<String, String> age_search_Hbase = hbaseService.getResultByHbase(age_search_up, field, "persona_summary_prod");
            int countAge = 0;
            for (Map.Entry<String, String> thisMap : age_search_Hbase.entrySet()) {
                int value = Integer.parseInt(thisMap.getValue());
                countAge += value;
            }
            List<DictModel> dmList = dictMapper.queryKVInfo("agebin");
            mom.setAgeBins(getResult(age_search_Hbase, dmList, countAge));
        }

        /**
         * 机型
         * 
         * @param personaSummary
         * @param countfield
         * @param field
         * @throws Exception
         */
        public void marketShareTrend(PersonaSummary personaSummary, String countfield, String field) throws Exception {
            List<String> monthList = overviewAllService.getMonthData(personaSummary.getMnt());
            HashMap<String, String> hm = new HashMap<>();
            CompletionService getResultFromHbaseService = new ExecutorCompletionService(threadPoolTaskExecutor);
            PersonaSummary personaSummary2 = new PersonaSummary("", "", personaSummary.getPrice(), personaSummary.getCountry(), personaSummary.getProvince(), personaSummary.getMnt());

            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(0), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(1), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(2), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(3), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(4), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary, countfield, monthList.get(5), field, "brandmole"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(0), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(1), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(2), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(3), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(4), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(5), field, "branddeno"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(0), field, "unknown"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(1), field, "unknown"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(2), field, "unknown"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(3), field, "unknown"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(4), field, "unknown"));
            getResultFromHbaseService.submit(new OverviewModelService.Task2(hm, personaSummary2, countfield, monthList.get(5), field, "unknown"));

            for (int i = 0; i < 18; i++) {
                Future<String> future = getResultFromHbaseService.take();
            }
            List<OverviewBaseModel> marketList = new ArrayList<OverviewBaseModel>();

            for (String month : monthList) {
                String mole = hm.get(month + "brandmole");
                if (mole != null) {
                    String deno = hm.get(month + "branddeno");
                    String unknown = hm.get(month + "unknown");
                    Double answer = Double.parseDouble(mole) / (Double.parseDouble(deno) - Double.parseDouble(unknown));
                    OverviewBaseModel bm = new OverviewBaseModel(month, answer);
                    marketList.add(bm);
                }
            }
            OverviewBaseListModel overviewBaseListModel = new OverviewBaseListModel(personaSummary.getModel(), marketList);
            List<OverviewBaseListModel> oblmList = new ArrayList<OverviewBaseListModel>();
            oblmList.add(overviewBaseListModel);
            mom.setMarket_share_model(oblmList);
        }

        public void gendetProportion(PersonaSummary personaSummary, String countfield, String field) throws Exception {
            Map<String, String> model_search_up = overviewESService.getRowKeyByEsForModel(personaSummary, countfield, OperationEnum.EQ.getOperation());
            model_search_up.remove("-1");
            Map<String, String> market_share_brand_up = hbaseService.getResultByHbase(model_search_up, field, "persona_summary_prod");
            int sumGender = Integer.parseInt(market_share_brand_up.get("1")) + Integer.parseInt(market_share_brand_up.get("0"));
            List<DictModel> dmList = dictMapper.queryKVInfo("gender");
            mom.setGenders(getResult(market_share_brand_up, dmList, sumGender));
        }

        public List<BaseModel> getResult(Map<String, String> theMap, List<DictModel> list, int sumCount) {
            List<BaseModel> resultList = new ArrayList();
            for (Map.Entry<String, String> thisMap : theMap.entrySet()) {
                String key = thisMap.getKey();
                for (DictModel dm : list) {
                    if (dm.getPer_key().equals(key)) {
                        String finalKey = dm.getPer_value();
                        Double val = Double.parseDouble(thisMap.getValue()) / sumCount;
                        BaseModel bm = new BaseModel(finalKey, val);
                        resultList.add(bm);
                    }
                }
            }
            return resultList;
        }
    }

    class Task2 implements Callable {

        private HashMap<String, String> map;
        private PersonaSummary          personaSummary;
        private String                  countfield;
        private String                  month;
        private String                  field;
        private String                  type;

        public Task2(HashMap map, PersonaSummary personaSummary, String countfield, String month, String field, String type) {
            this.map = map;
            this.personaSummary = personaSummary;
            this.countfield = countfield;
            this.month = month;
            this.field = field;
            this.type = type;
        }

        @Override
        public String call() {
            try {
                if (type.contains("brand")) {
                    PersonaSummary thisPersonSummary = new PersonaSummary(personaSummary.getModel(), personaSummary.getBrand(), personaSummary.getPrice_range(), personaSummary.getCountry(), personaSummary.getProvince(),
                                                                          month);
                    Map<String, String> result = overviewESService.getRowKeyByEsForModel(thisPersonSummary, countfield, OperationEnum.EQ.getOperation());
                    Map<String, String> market_share_brand = hbaseService.getResultByHbase(result, field, "persona_summary_prod");
                    map.put(month + type, market_share_brand.get(month));
                } else {
                    PersonaSummary unknownPersonSummary = new PersonaSummary(personaSummary.getModel(), personaSummary.getBrand(), personaSummary.getPrice_range(), personaSummary.getCountry(),
                                                                             personaSummary.getProvince(), month);
                    Map<String, String> result = overviewESService.getRowKeyByEsForUnknown(unknownPersonSummary, countfield, OperationEnum.EQ.getOperation());
                    Map<String, String> market_share_brand = hbaseService.getResultByHbase(result, field, "persona_summary_prod");
                    map.put(month + type, market_share_brand.get(month));
                }
                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }
}
