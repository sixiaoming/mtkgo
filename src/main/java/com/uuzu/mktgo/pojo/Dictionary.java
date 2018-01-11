package com.uuzu.mktgo.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zj_pc
 */
public class Dictionary {


    public static Map getIdType(String idCode) {
        return idTypeMap.get(idCode);
    }

    private static Map<String, Map<String, String>> idTypeMap = new HashMap<String, Map<String, String>>() {
        private static final long serialVersionUID = 1L;

        {
            Map genders = new HashMap();
            genders.put("0", "男");
            genders.put("1", "女");
            genders.put("-1", "未知");
            put("genders", genders);

            Map agebins = new HashMap();
            agebins.put("0", "七零前");
            agebins.put("1", "七零后");
            agebins.put("2", "八零后");
            agebins.put("3", "九零后");
            agebins.put("4", "零零后");
            agebins.put("-1", "未知");
            put("agebins", agebins);

            Map incomes = new HashMap();
            incomes.put("2", ">10k");
            incomes.put("1", "4k-10k");
            incomes.put("0", "<4k");
            incomes.put("-1", "未知");
            put("incomes", incomes);

            Map segment = new HashMap();
            segment.put("-1", "未知");
            segment.put("0", "出行达人");
            segment.put("1", "购物达人");
            segment.put("2", "买车一族");
            segment.put("3", "理财达人");
            segment.put("4", "健身达人");
            segment.put("5", "听新闻一族");
            segment.put("6", "美拍达人");
            segment.put("7", "空战游戏爱好者");
            segment.put("8", "求职招聘一族");
            segment.put("9", "时尚达人");
            segment.put("10", "买房一族");
            segment.put("11", "策略塔防游戏爱好者");
            segment.put("12", "手机卫士达人");
            segment.put("13", "煲剧一族");
            segment.put("14", "旅游达人");
            segment.put("15", "电影迷");
            segment.put("16", "纸牌游戏爱好者");
            segment.put("17", "动作游戏爱好者");
            segment.put("18", "对战游戏爱好者");
            segment.put("19", "早教一族");
            segment.put("20", "家政需求者");
            segment.put("21", "换装游戏爱好者");
            segment.put("22", "休闲游戏爱好者");
            segment.put("23", "保险达人");
            segment.put("24", "电子产品爱好者");
            segment.put("25", "体育爱好者");
            segment.put("26", "备孕一族");
            segment.put("27", "直播爱好者");
            segment.put("28", "脱口秀爱好者");
            segment.put("29", "办公达人");
            segment.put("30", "借贷一族");
            segment.put("31", "电子书爱好者");
            segment.put("32", "语言学习者");
            segment.put("33", "养颜达人");
            segment.put("34", "音乐爱好者");
            put("segment", segment);

            Map occupation = new HashMap();
            occupation.put("-1", "未知");
            put("occupation", occupation);

            Map edu = new HashMap();
            edu.put("0", "高中及以下");
            edu.put("1", "专科");
            edu.put("2", "本科");
            edu.put("-1", "未知");
            put("edu", edu);

            Map married = new HashMap();
            married.put("-1", "未知");
            put("married", married);

            Map kids = new HashMap();
            kids.put("-1", "未知");
            kids.put("0", "无");
            kids.put("1", "有");
            put("kids", kids);

            Map car = new HashMap();
            car.put("-1", "未知");
            put("car", car);

            Map house = new HashMap();
            house.put("-1", "未知");
            put("house", house);

            Map network = new HashMap();
            network.put("unknown", "unknown");
            network.put("2g", "2g");
            network.put("3g", "3g");
            network.put("4g", "4g");
            network.put("5g", "5g");
            network.put("wifi", "wifi");
            network.put("cell", "cell");
            network.put("other", "other");
            put("network",network);

        }
    };
}
