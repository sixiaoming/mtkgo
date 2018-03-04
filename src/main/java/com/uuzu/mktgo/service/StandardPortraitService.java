package com.uuzu.mktgo.service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.elasticsearch.PersonaSummary;
import com.uuzu.mktgo.mapper.DateMonthMapper;
import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.OperationEnum;
import com.uuzu.mktgo.pojo.StandardPortraitModel;

/**
 * 标准画像接口 V2.0 es+hbase架构
 *
 * @author zhoujin
 */
@Slf4j
@Service
public class StandardPortraitService {

    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private HbaseService         hbaseService;
    @Autowired
    DateMonthMapper              dateMonthMapper;
    @Autowired
    DictInfoService              dictInfoService;

    /**
     * 标准画像接口
     *
     * @param brand 品牌
     * @param model 机型
     * @param price 价位
     * @param country 国家
     * @param province 省份
     */
    public StandardPortraitModel standardPortrait(String brand, String model, String price, String country, String province) throws Exception {
        StandardPortraitModel standardPortraitModel = new StandardPortraitModel();

        // 获取最大日期
        String maxMonth = dateMonthMapper.queryMaxMonth();

        // 条件
        PersonaSummary personaSummary = new PersonaSummary(model, brand, price, country, province, maxMonth);

        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);
        long startTime = System.currentTimeMillis();// 记录开始时间
        completionService.submit(new Task(personaSummary, "gender", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "agebin", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "income", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "segment", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "edu", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "kids", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "car", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "house", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "network", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "married", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "occupation", "imei_count", standardPortraitModel));
        completionService.submit(new Task(personaSummary, "carrier", "imei_count", standardPortraitModel));

        for (int i = 0; i < 12; i++) {
            Future<String> future = completionService.take();
        }

        long endTime = System.currentTimeMillis();// 记录结束时间
        float excTime = (float) (endTime - startTime) / 1000;
        log.info("执行时间：" + excTime + "s");
        return standardPortraitModel;
    }

    public StandardPortraitModel convertModelByFieldAndMap(StandardPortraitModel standardPortraitModel, Map<String, String> map, String countfield) throws NoSuchFieldException, IllegalAccessException {
        // 获取所有的字典
        Map<String, Map<String, String>> dictInfo = dictInfoService.getDictInfo();

        long sum = 0l;
        // sum
        for (String key : map.keySet()) {
            sum += Long.parseLong(map.get(key));
        }

        // 计算比例并且将相应的key转成中文
        List<BaseModel> baseModels = new ArrayList<>();
        for (String key : map.keySet()) {
            if ("carrier".equals(countfield)) {
                baseModels.add(new BaseModel(key, Double.parseDouble(map.get(key)) / sum));

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

        if ("carrier".equals(countfield) && baseModels.size() > 10) {
            baseModels = baseModels.subList(0, 10);
        }

        // 通过字段反射设置对象的值
        if (map != null) {
            Field field = standardPortraitModel.getClass().getDeclaredField(countfield);
            field.setAccessible(true);
            field.set(standardPortraitModel, baseModels);
        }
        return standardPortraitModel;
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * fansPortrait task
     */
    class Task implements Callable<Object> {

        private PersonaSummary        personaSummary;
        private String                countfield;
        private String                field;
        private StandardPortraitModel standardPortraitModel;

        public Task(PersonaSummary personaSummary, String countfield, String field, StandardPortraitModel standardPortraitModel) {
            this.personaSummary = personaSummary;
            this.countfield = countfield;
            this.field = field;
            this.standardPortraitModel = standardPortraitModel;
        }

        @Override
        public String call() {
            try {
                // 先从es中获取相应的rowkey
                Map<String, String> esRowKeys = elasticsearchService.getRowKeyByEs(personaSummary, countfield, OperationEnum.EQ.getOperation());
                // 将相应的rowkey从hbase中获取对应的结果
                Map<String, String> hbaseResult = hbaseService.getResultByHbase(esRowKeys, field, "persona_summary_prod");
                // 将结果转换并计算百分比
                convertModelByFieldAndMap(standardPortraitModel, hbaseResult, countfield);

                return "success";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }
}
