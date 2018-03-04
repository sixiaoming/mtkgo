package com.uuzu.mktgo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.OverviewBaseListModel;
import com.uuzu.mktgo.pojo.OverviewBaseModel;

/**
 * @author zhoujin
 */
public class ConvertUtil {

    /***
     * 响应格式转换
     * 
     * @param content
     * @return
     */
    public static List<BaseModel> parse(String content, String field) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }

        double sum = 0.0;
        for (Map<String, Object> stringObjectMap : list) {
            sum += Double.parseDouble(stringObjectMap.get("imei_count").toString());
        }
        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        for (Map<String, Object> stringObjectMap : list) {
            // String key = (String) Dictionary.getIdType(field).get(stringObjectMap.get(field).toString());
            String key = stringObjectMap.get(field).toString();
            double value = Double.parseDouble(stringObjectMap.get("imei_count").toString());
            baseModels.add(new BaseModel(key, value / sum));
        }
        return baseModels;
    }

    /**
     * 响应格式转换
     * 
     * @param content
     * @param field
     * @param columnField
     * @return
     */
    public static List<BaseModel> parse(String content, String field, String columnField) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }

        double sum = 0.0;
        for (Map<String, Object> stringObjectMap : list) {
            sum += Double.parseDouble(stringObjectMap.get(columnField).toString());
        }
        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        for (Map<String, Object> stringObjectMap : list) {
            String key = stringObjectMap.get(field).toString();
            double value = Double.parseDouble(stringObjectMap.get(columnField).toString());
            baseModels.add(new BaseModel(key, value / sum));
        }
        return baseModels;
    }

    /***
     * 响应格式转换
     * 
     * @param content
     * @return
     */
    public static List<Map<String, Object>> parse(String content) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }
        return list;
    }

    /***
     * 响应格式转换 不百分比
     * 
     * @param content
     * @return
     */
    public static List<BaseModel> parseNotPercent(String content, String field) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }

        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        for (Map<String, Object> stringObjectMap : list) {
            // String key = (String) Dictionary.getIdType(field).get(stringObjectMap.get(field).toString());
            String key = stringObjectMap.get(field).toString();
            double value = Double.parseDouble(stringObjectMap.get("imei_count").toString());
            baseModels.add(new BaseModel(key, value));
        }
        return baseModels;
    }

    /***
     * 响应格式转换
     * 
     * @param content
     * @return
     */
    public static List<BaseModel> parse(String content, String field, Double sum) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }

        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        for (Map<String, Object> stringObjectMap : list) {
            // String key = (String) Dictionary.getIdType(field).get(stringObjectMap.get(field).toString());
            String key = stringObjectMap.get(field).toString();
            double value = Double.parseDouble(stringObjectMap.get("imei_count").toString());
            baseModels.add(new BaseModel(key, value / sum));
        }
        return baseModels;
    }

    /***
     * 响应格式转换
     * 
     * @param content
     * @param monthSumMap
     * @return
     */
    public static List<OverviewBaseListModel> parse1(String content, String field, Map<?, ?> monthSumMap) {
        if (content == null || StringUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if (jsonObject == null) {
            return null;
        }
        String result = jsonObject.getString("msg");
        if (result == null || !("success").equals(result)) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return null;
        }
        JSONArray array = data.getJSONArray("data");
        if (array == null || array.size() == 0) {
            return null;
        }

        JSONArray headers = array.getJSONArray(0);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 1; i < array.size(); i++) {
            JSONArray values = array.getJSONArray(i);
            map = new HashMap<String, Object>();
            for (int j = 0; j < values.size(); j++) {
                map.put(headers.getString(j), values.get(j));
            }
            list.add(map);
        }

        Map<String, List<OverviewBaseModel>> results = new HashMap<>();

        for (Map<String, Object> objectMap : list) {
            if (results.containsKey(objectMap.get(field))) {
                results.get(objectMap.get(field)).add(new OverviewBaseModel((String) objectMap.get("month"), (Integer) objectMap.get("imei_count")));
            } else {
                List<OverviewBaseModel> bases = new ArrayList<>();

                bases.add(new OverviewBaseModel((String) objectMap.get("month"), (Integer) objectMap.get("imei_count")));
                results.put((String) objectMap.get(field), bases);
            }

        }
        List<OverviewBaseListModel> overviewBaseListModels = new ArrayList<>();
        for (String s : results.keySet()) {
            List<OverviewBaseModel> sumlist = results.get(s);
            for (OverviewBaseModel overviewBaseModel : sumlist) {
                overviewBaseModel.setValue(overviewBaseModel.getValue() / (Double) (monthSumMap.get(overviewBaseModel.getDate())));
            }
            overviewBaseListModels.add(new OverviewBaseListModel(s, results.get(s)));
        }
        return overviewBaseListModels;
    }
}
