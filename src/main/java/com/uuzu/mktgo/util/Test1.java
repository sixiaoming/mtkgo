package com.uuzu.mktgo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhoujin
 */
public class Test1 {
    private static final MediaType MEDIA_TYPE_BY_JSON = MediaType.parse("application/json;charset=utf-8");

    public static void main(String[] args) throws IOException {
        String[] cat1s = {"服装品牌", "鞋业品牌", "皮具箱包", "配饰"};
        String[] cat2_1 = {"女装品牌", "男装品牌", "内衣品牌", "休闲品牌", "运动品牌", "皮草品牌", "恤衫品牌", "牛仔品牌", "羽绒服品牌", "职业装", "羊绒羊毛", "婚纱礼服", "中装、唐装、旗袍", "中老年服装"};
        String[] cat2_2 = {"女鞋", "男鞋", "休闲鞋", "运动鞋"};
        String[] cat2_3 = {"女包品牌", "旅行箱包", "皮具品牌"};
        String[] cat2_4 = {"黄金珠宝", "眼镜", "手表", "时尚饰品", "服饰配饰"};

        String[] cat3_1_1 = {"奢侈女装", "成熟女装", "轻奢女装", "少淑女装", "淑女装", "日系女装", "韩国女装", "台湾女装", "时尚女装", "休闲女装", "商务女装", "森系女装", "棉麻女装", "大码女装", "电商女装"};
        String[] cat3_1_2 = {"时尚男装", "休闲男装", "商务男装"};
        String[] cat3_1_3 = {"女性内衣", "少女内衣", "男士内衣", "情趣内衣", "基础内衣", "保暖内衣", "丝绸内衣", "家居服", "袜业", "泳装"};
        String[] cat3_1_4 = {};
        String[] cat3_1_5 = {"户外品牌", "运动服饰"};
        String[] cat3_1_6 = {};
        String[] cat3_1_7 = {};
        String[] cat3_1_8 = {};
        String[] cat3_1_9 = {};
        String[] cat3_1_10 = {};
        String[] cat3_1_11 = {};
        String[] cat3_1_12 = {};
        String[] cat3_1_13 = {};
        String[] cat3_1_14 = {};

        String[] cat4s = {"设计师品牌", "快时尚", "潮牌", "定制", "集合店"};


        String url = "http://www.chinasspp.com/brand/%s";
        String url2 = "http://www.chinasspp.com/brand/%s/%d/";
        String url3 = "http://www.chinasspp.com/brand/bsearch.aspx?cid=%S&tid=%s&area=&lid=&order=&key=&page=%d";
        File file = new File("/data/brand.txt");
        FileWriter fw = new FileWriter(file, true);
        ObjectMapper objectMapper = new ObjectMapper();
        for (String cat2 : cat3_1_1) {
            for (String cat4 : cat4s) {
                int i = 1;
                while (true) {
                    String pageUrl = String.format(url3, cat2, cat4, i);
                    try {
                        List<Brand> brands = getBrand(pageUrl, cat1s[0], cat2_1[0], cat2, cat4);
                        for (Brand brand : brands) {
                            fw.write(objectMapper.writeValueAsString(brand) + "\n");
                        }
                        System.out.println(pageUrl + ":    " + brands.size());
                        if (brands.size() < 20) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }

//                if(cat2.equals("配饰")&&i==110){
//                    break;
//                }
                    i++;
                }
            }
        }
        for (String cat2 : cat3_1_2) {
            for (String cat4 : cat4s) {
                int i = 1;
                while (true) {
                    String pageUrl = String.format(url3, cat2, cat4, i);
                    try {
                        List<Brand> brands = getBrand(pageUrl, cat1s[0], cat2_1[1], cat2, cat4);
                        for (Brand brand : brands) {
                            fw.write(objectMapper.writeValueAsString(brand) + "\n");
                        }
                        System.out.println(pageUrl + ":    " + brands.size());
                        if (brands.size() < 20) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }

//                if(cat2.equals("配饰")&&i==110){
//                    break;
//                }
                    i++;
                }
            }
        }
        for (String cat2 : cat3_1_3) {
            for (String cat4 : cat4s) {
                int i = 1;
                while (true) {
                    String pageUrl = String.format(url3, cat2, cat4, i);
                    try {
                        List<Brand> brands = getBrand(pageUrl, cat1s[0], cat2_1[2], cat2, cat4);
                        for (Brand brand : brands) {
                            fw.write(objectMapper.writeValueAsString(brand) + "\n");
                        }
                        System.out.println(pageUrl + ":    " + brands.size());
                        if (brands.size() < 20) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }

//                if(cat2.equals("配饰")&&i==110){
//                    break;
//                }
                    i++;
                }
            }
        }
        for (String cat2 : cat3_1_5) {
            for (String cat4 : cat4s) {
                int i = 1;
                while (true) {
                    String pageUrl = String.format(url3, cat2, cat4, i);
                    try {
                        List<Brand> brands = getBrand(pageUrl, cat1s[0], cat2_1[4], cat2, cat4);
                        for (Brand brand : brands) {
                            fw.write(objectMapper.writeValueAsString(brand) + "\n");
                        }
                        System.out.println(pageUrl + ":    " + brands.size());
                        if (brands.size() < 20) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }

//                if(cat2.equals("配饰")&&i==110){
//                    break;
//                }
                    i++;
                }
            }
        }

    }

    public static List<Brand> getBrand(String pageUrl, String cat1, String cat2, String cat3, String cat4) throws IOException {

        List<Brand> brandList = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient();


        RequestBody body = RequestBody.create(MEDIA_TYPE_BY_JSON, "");
        Request request = new Request.Builder().url(pageUrl).get().build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            System.err.println("error");
            return null;
        }
        String result = response.body().string();
        Document doc = Jsoup.parse(result, pageUrl);

        Elements brands = doc.select(".brand");
        Iterator divs = brands.iterator();
        while (divs.hasNext()) {
            Brand brand = new Brand();
            brand.setCat1(cat1);
            brand.setCat2(cat2);
            brand.setCat3(cat3);
            Element element = (Element) divs.next();
            brand.setBrandName(element.select(".first").select("a").text());
            String name = element.select(".first").select("p").text();
            String value = element.select(".first").select("span").text();

            brand.setCompany(name.replace(value, ""));
            brand.setCatName(value);

            String name1 = element.select(".last").select("i").text();
            String value1 = element.select(".last").select("p").text();
            brand.setHot(name1);
            brand.setLocation(value1.replace(name1, ""));
            brand.setCat4(cat4);
            brandList.add(brand);
        }
        return brandList;
    }


    public void test() {

    }

    @Data
    public static class Brand {
        private String cat1;
        private String cat2;
        private String cat3;
        private String brandName;
        private String company;
        private String catName;
        private String location;
        private String hot;
        private String cat4;
    }

}
