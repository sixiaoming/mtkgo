package com.uuzu.mktgo.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhoujin
 */
public class SqlUtil {

    /**
     * 动态拼接sql条件
     *
     * @param brand 品牌
     * @param model 机型
     * @param price 价位
     * @param country 国家
     * @param province 省份
     * @param field 字段
     * @return
     */
    public static String getSqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth) throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(imei_count) as imei_count" + " from rp_mobeye_mktgo.persona_summary where month = '" + maxMonth + "'");
        // rp_sdk_mktgo.persona_summary
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        return sqlTemplete.toString();
    }

    /**
     * get sql by dynamic condition
     * 
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param province
     * @param field
     * @param maxMonth
     * @param table
     * @return
     * @throws IOException
     */
    public static String getSqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String table, String columnField, String monthField)
                                                                                                                                                                                                                throws IOException {

        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(" + columnField + ") as " + columnField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        return sqlTemplete.toString();
    }

    /**
     * get sql by dynamic condition
     * 
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param province
     * @param field
     * @param maxMonth
     * @param table
     * @return
     * @throws IOException
     */
    public static String getNewSqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String table, String columnField, String monthField)
                                                                                                                                                                                                                   throws IOException {

        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(" + columnField + ") as " + columnField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand_new = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model_new = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range_new = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        return sqlTemplete.toString();
    }

    /**
     * 动态拼接sql条件
     *
     * @param brand 品牌
     * @param model 机型
     * @param price 价位
     * @param country 国家
     * @param province 省份
     * @return
     */
    public static String getSqlByDynamicCondition(String brand, String model, String price, String country, String province) throws IOException {
        StringBuffer sqlTemplete = new StringBuffer(" 1=1 ");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        return sqlTemplete.toString();
    }

    /**
     * 获取top10 sql
     * 
     * @param brand 品牌
     * @param model 机型
     * @param price 价位
     * @param country 国家
     * @param province 省份
     * @param field 字段
     * @param maxMonth 月份
     * @param sumField 统计字段
     * @param wheresql where条件
     * @return
     * @throws IOException
     */
    public static String getTop10SqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String sumField, String wheresql) throws IOException {
        String sql = getAllProvinceSqlByDynamicCondition(brand, model, price, country, province, field, maxMonth, sumField, wheresql) + " limit 10";

        return sql;
    }

    /**
     * 获取全部 sql
     * 
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param province
     * @param field
     * @param maxMonth
     * @param sumField
     * @param wheresql
     * @return
     * @throws IOException
     */
    public static String getAllProvinceSqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String sumField, String wheresql) throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(" + sumField + ") as imei_count" + " from rp_mobeye_mktgo.persona_summary where " + wheresql + " and month = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        sqlTemplete.append(" order by imei_count desc ");
        return sqlTemplete.toString();
    }

    // @Test
    public void test() throws IOException {
        SqlUtil.getTop10SqlByDynamicCondition("", "", "", "", "", "brand", "201708", "imei_count", "brand<>'other' and brand<>'unknown'");
    }

    public static String getSourceSumSqlByDynamicCondition(String brand, String model, String price, String country, String province, String maxMonth, String sumField, String table, String monthField) throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select sum(" + sumField + ") as " + sumField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand_new = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model_new = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range_new = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        return sqlTemplete.toString();
    }

    public static String getSourceTop10SqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String sumField, String table, String monthField)
                                                                                                                                                                                                                        throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(" + sumField + ") as " + sumField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand_new = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model_new = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range_new = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        sqlTemplete.append(" order by " + sumField + " desc  limit 10");
        return sqlTemplete.toString();
    }

    public static String getTrendSumSqlByDynamicCondition(String brand, String model, String price, String country, String province, String maxMonth, String sumField, String table, String monthField) throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select sum(" + sumField + ") as " + sumField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        return sqlTemplete.toString();
    }

    public static String getTrendTop10SqlByDynamicCondition(String brand, String model, String price, String country, String province, String field, String maxMonth, String sumField, String table, String monthField)
                                                                                                                                                                                                                       throws IOException {
        StringBuffer sqlTemplete = new StringBuffer("select " + field + " ,sum(" + sumField + ") as " + sumField + " from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" group by " + field);
        sqlTemplete.append(" order by " + sumField + " desc  limit 10");
        return sqlTemplete.toString();
    }

    /**
     * get changePhonePeriodSql
     * 
     * @param brand
     * @param model
     * @param price
     * @param country
     * @param province
     * @param table
     * @param monthField
     * @param maxMonth
     * @return
     */
    public static String getChangePhonePeriodSqlByDynamicCondition(String brand, String model, String price, String country, String province, String table, String monthField, String maxMonth, String month) {
        StringBuffer sqlTemplete = new StringBuffer("select month,sum(change_times_count) as change_times_count,sum(duration_days) as duration_days from " + table + " where " + monthField + " = '" + maxMonth + "'");
        if (StringUtils.isNotBlank(brand)) {
            sqlTemplete.append(" and brand = '" + brand + "'");
        }
        if (StringUtils.isNotBlank(model)) {
            sqlTemplete.append(" and model = '" + model + "'");
        }
        if (StringUtils.isNotBlank(price)) {
            sqlTemplete.append(" and price_range = '" + price + "'");
        }
        if (StringUtils.isNotBlank(country)) {
            sqlTemplete.append(" and country = '" + country + "'");
        }
        if (StringUtils.isNotBlank(province)) {
            sqlTemplete.append(" and province = '" + province + "'");
        }
        sqlTemplete.append(" and month >= '" + month + "'");
        sqlTemplete.append(" group by month");
        return sqlTemplete.toString();
    }

}
