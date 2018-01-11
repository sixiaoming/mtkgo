package com.uuzu.mktgo.service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.elasticsearch.PersonaSummary_final;
import com.uuzu.mktgo.mapper.BrandModelMappingNewMapper;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.*;
import com.uuzu.mktgo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import org.apache.avro.generic.GenericData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * @author zhongqi
 */
@Service
@Slf4j
public class OverviewAllService {
    @Autowired
    private BrandModelMappingNewMapper brandModelMappingNewMapper;
    @Autowired
    private OverviewESService          overviewESService;
    @Autowired
    private HbaseService               hbaseService;
    @Autowired
    DateMonthMapper               dateMonthMapper;
    @Autowired
    DictInfoService               dictInfoService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 首页概览
     * V2.0
     * es+hbase架构
     *
     * @param price
     * @param country
     * @param province
     * @param date
     * @return
     * @throws Exception
     */
    public OverviewModel overviewAll(String price, String country, String province, String date) throws Exception {

        OverviewModel om =new OverviewModel();
        String endDate = null;
        if (StringUtils.isBlank(date)) {
            endDate = dateMonthMapper.queryMaxMonth();
        } else {
            endDate = date;
        }
        String startDate = DateUtil.getLastSixMonthDate(endDate);
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);
        PersonaSummary_final personaSummary = new PersonaSummary_final("", "", price, country, province, startDate+"|"+endDate);
        Map <String, String>  result=overviewESService.getRowKeyFinal(personaSummary, "mnt", OperationEnum.BET.getOperation());
        completionService.submit(new OverviewAllService.Task(result,personaSummary, "brand_name",  "brand",om));
        completionService.submit(new OverviewAllService.Task(result,personaSummary, "model",  "model",om));
        completionService.submit(new OverviewAllService.Task(result,personaSummary,"brand_name",  "holdingRank",om));
        completionService.submit(new OverviewAllService.Task(result,personaSummary,"model",  "sellingRank",om));
        completionService.submit(new OverviewAllService.Task(result,personaSummary,"screensize",  "px",om));
        completionService.submit(new OverviewAllService.Task(result,personaSummary,"sysver",  "os",om));
        for (int i = 0; i < 6; i++) {
            Future<String> future = completionService.take();
        }
        return om;
    }

    public String getValueByField(PersonaSummary personaSummary, String countfield) throws Exception {
        Field field = personaSummary.getClass().getDeclaredField(countfield);
        field.setAccessible(true);
        return field.get(personaSummary).toString();
    }

    public int topSum(Map<String,String> soulist ){
        int count = 0;
        for (Map.Entry<String, String> thisMap : soulist.entrySet()) {
            if(thisMap.getValue() != null && !thisMap.getValue().equals("null")){
                count+=Double.parseDouble(thisMap.getValue());
            }
        }
        return count;
    }

    public List<BaseModel> top10List(Map<String,String> soulist){
        //Map<String,String> soulist=null;
        if(soulist.get("unknown") != null ){
            soulist.remove("unknown");
        }
        List<BaseModel> returList = new ArrayList();
        for (Map.Entry<String, String> thisMap : soulist.entrySet()) {
            if(thisMap.getValue() != null && !thisMap.getValue().equals("null") ){
                BaseModel bm =new BaseModel(thisMap.getKey(),Double.parseDouble(thisMap.getValue()));
                returList.add(bm);
            }
        }
        Collections.sort(returList, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel o1, BaseModel o2) {
                return ((int)(o2.getValue())-(int)(o1.getValue()));
            }
        });

        if(returList.size()>9){
            returList = returList.subList(0,10);
        }else{
            returList = returList.subList(0,returList.size());
        }
        return returList;
    }
    public List<BaseModel> top10percentList(Map<String,String> soulist,int count ){

        if(soulist.get("unknown") != null){
            soulist.remove("unknown");
        }


        List<BaseModel> returList = new ArrayList();
        for (Map.Entry<String, String> thisMap : soulist.entrySet()) {
            if(thisMap.getValue() != null && !thisMap.getValue().equals("null") ){
                BaseModel bm =new BaseModel(thisMap.getKey(),Double.parseDouble(thisMap.getValue())/count);
                returList.add(bm);
            }
        }
        Collections.sort(returList, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel o1, BaseModel o2) {
                return ((int)(o2.getValue()*100000)-(int)(o1.getValue()*100000));
            }
        });
        if(returList.size()>9){
            returList = returList.subList(0,10);
        }else{
            returList = returList.subList(0,returList.size());
        }
        return returList;
    }
    public List<String> getMonthData(String endMonth)throws  Exception{
        String month6 = endMonth.split("\\|")[1];
        String month5 = DateUtil.getLastMonthDate(month6);
        String month4 = DateUtil.getLastMonthDate(month5);
        String month3 = DateUtil.getLastMonthDate(month4);
        String month2 = DateUtil.getLastMonthDate(month3);
        String month1 = DateUtil.getLastMonthDate(month2);
        List<String> monthList = new ArrayList<String>();
        monthList.add(month1);
        monthList.add(month2);
        monthList.add(month3);
        monthList.add(month4);
        monthList.add(month5);
        monthList.add(month6);
        return monthList;
    }
    public Map<String, String> getResultMap(List<PersonaSummary> search,String countfield)throws  Exception{
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return result;
    }
    public Map<String, String> getResultMap(Page<PersonaSummary> search,String countfield)throws  Exception{
        Map<String, String> result = new HashMap<>();
        for (PersonaSummary summary : search) {
            result.put(getValueByField(summary, countfield), summary.getRow_key().replace("\u0000", "\u0001"));
        }
        return result;
    }
    class Task implements Callable {
        private Map <String, String>  result;
        private PersonaSummary_final personaSummary;
        private String         countfield;
        private String          type;
        private OverviewModel   om;

        public Task(Map <String, String>  result,PersonaSummary_final personaSummary, String countfield, String type,OverviewModel om) {
            this.personaSummary =personaSummary;
            this.countfield = countfield;
            this.type = type;
            this.om=om;
            this.result=result;
        }

        @Override
        public String call() {
            try {
                if (type.equals("brand") ) {
                    this.marketShareBrand(result,countfield);
                }else if (type.equals("model") ){
                    this.marketShareModel(result,countfield);
                }else if(type.equals("holdingRank")){
                    this.holdingRank(result,countfield);
                }else if(type.equals("sellingRank")){
                    this.hot_selling_rank(result,countfield);
                }else if(type.equals("px")){
                    this.px_rank(result,countfield);
                }else if(type.equals("os")){
                    this.os_rank(result,countfield);
                }
                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        //品牌市场份额趋势
        public void marketShareBrand(Map <String, String>  result,String countfield) throws Exception {
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            Map<String, String> market_share_model = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            Map<String ,Map<String,String>> finalMap = new HashMap<String ,Map<String,String>>();
            for(String month:mobthList){
                Map<String, String> map=new HashMap<String,String>();
                if(market_share_model.get(month) != null && market_share_model.get(month).length()>0){
                    List<String> keys = Arrays.asList(market_share_model.get(month).split("\u0003")[0].split("\u0002"));
                    List<String> values = Arrays.asList(market_share_model.get(month).split("\u0003")[1].split("\u0002"));
                    for(int i=0;i<keys.size();i++){
                        map.put(keys.get(i),values.get(i));
                    }
                }
                finalMap.put(month,map);
            }
            Map<String, String> market_share_brand1=finalMap.get(mobthList.get(0));
            Map<String, String> market_share_brand2=finalMap.get(mobthList.get(1));
            Map<String, String> market_share_brand3=finalMap.get(mobthList.get(2));
            Map<String, String> market_share_brand4=finalMap.get(mobthList.get(3));
            Map<String, String> market_share_brand5=finalMap.get(mobthList.get(4));
            Map<String, String> market_share_brand6=finalMap.get(mobthList.get(5));

            List<BaseModel> brand_top10 = top10List(market_share_brand6);
            //取出前十品牌放入单独集合中
            List<OverviewBaseListModel> OBLMlist =new ArrayList<>();
            for (BaseModel bm : brand_top10) {
                List<OverviewBaseModel> bomList = new ArrayList<OverviewBaseModel>();
                String market_share_brand_month1 = market_share_brand1.get(bm.getKey());//第1个月
                OverviewBaseModel obm1=null;
                if(null != market_share_brand_month1){
                    Double occupanc1 = Double.parseDouble(market_share_brand_month1) / topSum(market_share_brand1); //占有率
                    obm1=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(0),occupanc1);
                    bomList.add(obm1);
                }
                String market_share_brand_month2 = market_share_brand2.get(bm.getKey());//第2个月
                OverviewBaseModel obm2=null;
                if(null !=market_share_brand_month2 ){
                    Double occupanc2 = Double.parseDouble(market_share_brand_month2) / topSum(market_share_brand2); //占有率
                    obm2=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(1),occupanc2);
                    bomList.add(obm2);
                }
                String market_share_brand_month3 = market_share_brand3.get(bm.getKey());//第3个月
                OverviewBaseModel obm3=null;
                if(null != market_share_brand_month3 ){
                    Double occupanc3 = Double.parseDouble(market_share_brand_month3) / topSum(market_share_brand3); //占有率
                    obm3=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(2),occupanc3);
                    bomList.add(obm3);
                }
                String market_share_brand_month4 = market_share_brand4.get(bm.getKey());//第4个月
                OverviewBaseModel obm4=null;
                if(null != market_share_brand_month4){
                    Double occupanc4 = Double.parseDouble(market_share_brand_month4) / topSum(market_share_brand4); //占有率
                    obm4 =new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(3),occupanc4);
                    bomList.add(obm4);
                }
                String market_share_brand_month5 = market_share_brand5.get(bm.getKey());//第5个月
                OverviewBaseModel obm5=null;
                if(null != market_share_brand_month5){
                    Double occupanc5 = Double.parseDouble(market_share_brand_month5) / topSum(market_share_brand5); //占有率
                     obm5=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(4),occupanc5);
                        bomList.add(obm5);
                }
                String market_share_brand_month6 = market_share_brand6.get(bm.getKey());//第6个月
                OverviewBaseModel obm6=null;
                if(null != market_share_brand_month6){
                    Double occupanc6 = Double.parseDouble(market_share_brand_month6) / topSum(market_share_brand6);; //占有率
                    obm6=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(5),occupanc6);
                    bomList.add(obm6);
                }
                OverviewBaseListModel OverviewBaseListModel =new OverviewBaseListModel(bm.getKey(),bomList);
                OBLMlist.add(OverviewBaseListModel);
            }
            om.setMarket_share_brand(OBLMlist);
        }
        /**
         * 机型市场份额趋势
         * @throws Exception
         */
        public void marketShareModel(Map <String, String>  result,String countfield) throws Exception {
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            Map<String, String> market_share_model = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            Map<String ,Map<String,String>> finalMap = new HashMap<String ,Map<String,String>>();
            for(String month:mobthList){
                Map<String, String> map=new HashMap<String,String>();
                if(market_share_model.get(month) != null &&market_share_model.get(month).length()>0){
                    List<String> keys = Arrays.asList(market_share_model.get(month).split("\u0003")[0].split("\u0002"));
                    List<String> values = Arrays.asList(market_share_model.get(month).split("\u0003")[1].split("\u0002"));
                    for(int i=0;i<keys.size();i++){
                        map.put(keys.get(i),values.get(i));
                    }
                    //finalMap.put(month,map);
                }
                    finalMap.put(month,map);

            }
            Map<String, String> market_share_model1=finalMap.get(mobthList.get(0));
            Map<String, String> market_share_model2=finalMap.get(mobthList.get(1));
            Map<String, String> market_share_model3=finalMap.get(mobthList.get(2));
            Map<String, String> market_share_model4=finalMap.get(mobthList.get(3));
            Map<String, String> market_share_model5=finalMap.get(mobthList.get(4));
            Map<String, String> market_share_model6=finalMap.get(mobthList.get(5));


            List<BaseModel> model_top10 =top10List(market_share_model6);
            List<OverviewBaseListModel> OBLMlist =new ArrayList<>();
            for(BaseModel bm:model_top10) {
                List<OverviewBaseModel> bomList = new ArrayList<OverviewBaseModel>();
                String market_share_model_month1 = market_share_model1.get(bm.getKey());//第1个月
                OverviewBaseModel obm1=null;
                if(null != market_share_model_month1){
                    Double occupancy1 = Double.parseDouble(market_share_model_month1) / topSum(market_share_model1); //占有率
                    obm1=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(0),occupancy1);
                    bomList.add(obm1);
                }
                String market_share_model_month2 = market_share_model2.get(bm.getKey());//第2个月
                OverviewBaseModel obm2=null;
                if(null != market_share_model_month2){
                    Double occupancy2 = Double.parseDouble(market_share_model_month2) / topSum(market_share_model2); //占有率
                    obm2 =new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(1),occupancy2);
                    bomList.add(obm2);
                }
                String market_share_model_month3 = market_share_model3.get(bm.getKey());//第3个月
                OverviewBaseModel obm3=null;
                 //占有率
                if(null !=market_share_model_month3 ){
                    Double occupancy3 = Double.parseDouble(market_share_model_month3) / topSum(market_share_model3);
                    obm3=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(2),occupancy3);
                    bomList.add(obm3);
                }
                String market_share_model_month4 = market_share_model4.get(bm.getKey());//第4个月
                OverviewBaseModel obm4=null;
                if(null !=market_share_model_month4 ){
                    Double occupancy4 = Double.parseDouble(market_share_model_month4) / topSum(market_share_model4); //占有率
                    obm4=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(3),occupancy4);
                    bomList.add(obm4);
                }
                String market_share_model_month5 = market_share_model5.get(bm.getKey());//第5个月
                OverviewBaseModel obm5=null;
                if(null != market_share_model_month5){
                    Double occupancy5 = Double.parseDouble(market_share_model_month5) / topSum(market_share_model5); //占有率
                    obm5=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(4),occupancy5);
                    bomList.add(obm5);
                }
                String market_share_model_month6 = market_share_model6.get(bm.getKey());//第6个月
                OverviewBaseModel obm6=null;
                if(null !=  market_share_model_month6){
                    Double occupancy6 = Double.parseDouble(market_share_model_month6) / topSum(market_share_model6);
                    obm6=new OverviewBaseModel(getMonthData(personaSummary.getMnt()).get(5),occupancy6);
                    bomList.add(obm6);
                }
                OverviewBaseListModel OverviewBaseListModel =new OverviewBaseListModel(bm.getKey(),bomList);
                OBLMlist.add(OverviewBaseListModel);
            }
            om.setMarket_share_model(OBLMlist);
        }
        /**
         * 品牌保有量
         * @throws Exception
         */
        public void holdingRank(Map <String, String>  result,String countfield) throws Exception{
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            String lastMonth = mobthList.get(mobthList.size()-1);
            Map<String, String>  holding_rank_HBase = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            Map<String, String>  holding_rank_HBase2 =new HashMap<String,String>();
            List<String> keys = Arrays.asList(holding_rank_HBase.get(lastMonth).split("\u0003")[0].split("\u0002"));
            List<String> values = Arrays.asList(holding_rank_HBase.get(lastMonth).split("\u0003")[1].split("\u0002"));
            for(int i=0;i<keys.size();i++){
                holding_rank_HBase2.put(keys.get(i),values.get(i));
            }
            List<BaseModel> holding_rank_list_top10 =top10percentList(holding_rank_HBase2,topSum(holding_rank_HBase2));
            om.setHolding_rank(holding_rank_list_top10);
        }
        /**
         * 热销机型排名 价格和占比
         * @throws Exception
         */
        public void hot_selling_rank(Map <String, String>  result,String countfield) throws Exception{
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            String lastMonth = mobthList.get(mobthList.size()-1);
            Map<String, String>  hot_selling_rank_result_HBase = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            //前十的机型
            Map<String, String>  hot_selling_rank_result_HBase2 =new HashMap<String,String>();
            Map<String, String>  hot_selling_rank_price_HBase2 =new HashMap<String,String>();
            List<String> keys = Arrays.asList(hot_selling_rank_result_HBase.get(lastMonth).split("\u0003")[0].split("\u0002"));
            List<String> values = Arrays.asList(hot_selling_rank_result_HBase.get(lastMonth).split("\u0003")[1].split("\u0002"));
            for(int i=0;i<keys.size();i++){
                hot_selling_rank_result_HBase2.put(keys.get(i),values.get(i));
            }
            List<BaseModel> hot_selling_rank_result_list_top10 =top10percentList(hot_selling_rank_result_HBase2,topSum(hot_selling_rank_result_HBase2));


            List<BaseModel> hot_selling_rank_result_price_top10=new ArrayList<BaseModel>();
            for(BaseModel bm:hot_selling_rank_result_list_top10){

                bm.getKey();
                String price = brandModelMappingNewMapper.queryPrice(bm.getKey());
                BaseModel thisBM =new BaseModel(bm.getKey(),Double.parseDouble(price));
                hot_selling_rank_result_price_top10.add(thisBM);
            }

            om.setHot_selling_rank(hot_selling_rank_result_list_top10);
            om.setHot_sell_price_rank(hot_selling_rank_result_price_top10);
        }

        /**
         * 屏幕分辨率排名
         * @throws Exception
         */
        public void px_rank(Map <String, String>  result,String countfield) throws Exception{
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            String lastMonth = mobthList.get(mobthList.size()-1);
            Map<String, String>  px_rank_result_HBase = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            Map<String, String>  px_rank_result_HBase2 =new HashMap<>();
            List<String> keys = Arrays.asList(px_rank_result_HBase.get(lastMonth).split("\u0003")[0].split("\u0002"));
            List<String> values = Arrays.asList(px_rank_result_HBase.get(lastMonth).split("\u0003")[1].split("\u0002"));
            for(int i=0;i<keys.size();i++){
                px_rank_result_HBase2.put(keys.get(i),values.get(i));
            }
            List<BaseModel> px_rank_list_top10 =top10percentList(px_rank_result_HBase2,topSum(px_rank_result_HBase2));
            om.setPx_rank(px_rank_list_top10);
        }

        /**
         * 操作系统排名
         * @throws Exception
         */
        public void os_rank(Map <String, String>  result,String countfield) throws Exception{
            List<String> mobthList = getMonthData(personaSummary.getMnt());
            String lastMonth = mobthList.get(mobthList.size()-1);
            Map<String, String>  os_rank_result_HBase = hbaseService.getResultByHbase(result, countfield,"overview_all_prod");
            Map<String, String>  os_rank_result_HBase2 =new HashMap<>();
            List<String> keys = Arrays.asList(os_rank_result_HBase.get(lastMonth).split("\u0003")[0].split("\u0002"));
            List<String> values = Arrays.asList(os_rank_result_HBase.get(lastMonth).split("\u0003")[1].split("\u0002"));
            for(int i=0;i<keys.size();i++){
                os_rank_result_HBase2.put(keys.get(i),values.get(i));
            }
            List<BaseModel> os_rank_list_top10 =top10percentList(os_rank_result_HBase2,topSum(os_rank_result_HBase2));
            om.setOs_rank(os_rank_list_top10);
        }
    }
}