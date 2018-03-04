package dzdp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import okhttp3.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhoujin
 */
public class Test {

    private static final MediaType MEDIA_TYPE_BY_JSON = MediaType.parse("application/json;charset=utf-8");

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OkHttpClient httpClient = new OkHttpClient();
        String url = "http://www.dianping.com/shopall/%d/0";
        File file = new File("/data/dazhongdianping.txt");
        FileWriter fw = new FileWriter(file, true);
        for (int i = 1; i <= 100; i++) {
            A a = new A();

            String geturl = String.format(url, i);
            RequestBody body = RequestBody.create(MEDIA_TYPE_BY_JSON, "");
            Request request = new Request.Builder().url(geturl).post(body).build();
            Call call = httpClient.newCall(request);
            Response response = call.execute();
            if (!response.isSuccessful()) {
                throw new IOException("unexpected code" + response);
            }
            String result = response.body().string();
            Document doc = Jsoup.parse(result, geturl);

            Elements citye = doc.select(".shopall");
            String city = citye.text();
            a.setCity(city);
            a.setCity_code(i + "");

            Elements div = doc.select(".shopallCate");
            Iterator divs = div.iterator();
            while (divs.hasNext()) {
                Element element = (Element) divs.next();
                a.setType(element.select("h2").text());

                Elements lists = element.select(".list");
                Iterator listIterator = lists.iterator();
                while (listIterator.hasNext()) {
                    Element list = (Element) listIterator.next();
                    a.setLevel_1(list.select(".Bravia").text());
                    a.setLevel_1_code(list.select(".Bravia").attr("href").split("/")[4]);

                    Elements elements = list.select(".B");
                    Iterator it = elements.iterator();

                    while (it.hasNext()) {
                        Element aa = (Element) it.next();
                        String href = aa.select("a").attr("href");
                        String text = aa.select("a").text();
                        a.setUrl(href);
                        a.setLevel_2(text);
                        a.setLevel_2_code(href.split("/")[5]);
                        fw.write(objectMapper.writeValueAsString(a) + "\n");
                    }

                }

            }

            System.err.println(i);
        }
    }

    public static class A {

        String city;
        String city_code;
        String type;
        String level_1;
        String level_1_code;
        String level_2;
        String level_2_code;
        String url;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLevel_1() {
            return level_1;
        }

        public void setLevel_1(String level_1) {
            this.level_1 = level_1;
        }

        public String getLevel_1_code() {
            return level_1_code;
        }

        public void setLevel_1_code(String level_1_code) {
            this.level_1_code = level_1_code;
        }

        public String getLevel_2() {
            return level_2;
        }

        public void setLevel_2(String level_2) {
            this.level_2 = level_2;
        }

        public String getLevel_2_code() {
            return level_2_code;
        }

        public void setLevel_2_code(String level_2_code) {
            this.level_2_code = level_2_code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
