package com.uuzu.mktgo.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhoujin
 */
@Slf4j
@Service
public class HttpUtil {

    private static final MediaType MEDIA_TYPE_BY_JSON = MediaType.parse("application/json;charset=utf-8");
    private static final String    HIVE_API_URL       = "http://10.6.30.199:8888/mobeye-bi/api/query";

    @Autowired
    private OkHttpClient           okHttpClient;

    private static HttpUtil        httpUtil;

    @PostConstruct
    public void init() {
        httpUtil = this;
        httpUtil.okHttpClient = this.okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 发送http请求
     *
     * @param sql
     * @return
     * @throws IOException
     */

    public static String sendHttpClient(String sql) throws IOException {

        long start = System.currentTimeMillis();

        JSONObject param = new JSONObject();
        param.put("project", "mktgo");
        param.put("sql", sql);
        System.err.println(sql);
        String result = null;
        RequestBody body = RequestBody.create(MEDIA_TYPE_BY_JSON, param.toJSONString());
        Request request = new Request.Builder().url(HIVE_API_URL).post(body).build();
        Call call = httpUtil.okHttpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException("unexpected code" + response);
        }
        result = response.body().string();

        long end = System.currentTimeMillis();
        log.info("sql is " + sql + "time cost is " + (end - start));
        return result;
    }
}
