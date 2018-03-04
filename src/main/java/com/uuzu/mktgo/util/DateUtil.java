/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author zj_pc
 */
public class DateUtil {

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyyMM
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getLastMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) - 1;
        if (month < 10) {
            return year + "0" + month;
        } else {
            return year + "" + month;
        }

    }

    /**
     * get max date from table
     */
    public static String getMaxMonth(String sql) throws IOException {
        String maxMonthStr = HttpUtil.sendHttpClient(sql);
        JSONObject jsonObject = JSON.parseObject(maxMonthStr);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray array = data.getJSONArray("data");
        String maxMonth = (String) array.getJSONArray(1).get(0);
        return maxMonth;
    }

    public static Date parseString2Date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLastSixMonthDate(String dateStr) throws ParseException {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = null;
        date = sdf.parse(dateStr);// 初始日期
        c.setTime(date);// 设置日历时间
        c.add(Calendar.MONTH, -5);// 在日历的月份上增加6个月
        return sdf.format(c.getTime());
    }

    public static String getLastMonthDate(String dateStr) throws ParseException {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = null;
        date = sdf.parse(dateStr);// 初始日期
        c.setTime(date);// 设置日历时间
        c.add(Calendar.MONTH, -1);// 在日历的月份上增加6个月
        return sdf.format(c.getTime());
    }

    public static String getLastMonthDate(String dateStr, int before) throws ParseException {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = null;
        date = sdf.parse(dateStr);// 初始日期
        c.setTime(date);// 设置日历时间
        c.add(Calendar.MONTH, -before);// 在日历的月份上增加before
        return sdf.format(c.getTime());
    }

    public static String[] getLastSixMonthDate(String dateStr, int monthBefore) throws ParseException {
        String[] dateStrs = new String[monthBefore];
        dateStrs[0] = dateStr;
        for (int i = 1; i < monthBefore; i++) {
            Calendar c = Calendar.getInstance();// 获得一个日历的实例
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = null;
            date = sdf.parse(dateStr);// 初始日期
            c.setTime(date);// 设置日历时间
            c.add(Calendar.MONTH, -i);// 在日历的月份上增加6个月

            dateStrs[i] = sdf.format(c.getTime());
        }

        return dateStrs;
    }

    public static void main(String[] args) throws ParseException {

        String[] a = getLastSixMonthDate("201710", 6);
        for (String s : a) {
            System.out.println(s);
        }

    }
}
