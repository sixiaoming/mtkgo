/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.cloud.client.SpringCloudApplication;
// import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lamfire.json.JSON;
import com.lamfire.utils.DateFormatUtils;

/**
 * @author zj_pc Mar 4, 2018 5:53:09 PM
 */
@SpringBootApplication
// @SpringCloudApplication
// @EnableFeignClients
@EnableAutoConfiguration
@EnableCaching
@PropertySource(value = "classpath:application.properties")
public class MarketGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketGoApplication.class, args);
    }

    /**
     * 健康检查
     * 
     * @return
     */
    @Bean
    public ServletRegistrationBean healthAction() {
        return new ServletRegistrationBean(new HttpServlet() {

            private static final long serialVersionUID = -55776623249934740L;

            @Override
            public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                JSON json = new JSON();
                json.put("status", 200);
                json.put("time", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS"));
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println(json.toJSONString());
                out.flush();
                out.close();
            }

            @Override
            public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                doGet(request, response);
            }
        }, "date", "/health");
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()//
        .connectTimeout(5, TimeUnit.SECONDS)//
        .readTimeout(5, TimeUnit.SECONDS)//
        .writeTimeout(5, TimeUnit.SECONDS)//
        .retryOnConnectionFailure(false)//
        .connectionPool(new ConnectionPool(500, 30, TimeUnit.MINUTES)).build();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(200);
        threadPoolTaskExecutor.setMaxPoolSize(500);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    // @Bean
    // public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    // PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
    // c.setIgnoreUnresolvablePlaceholders(true);
    // return c;
    // }
}
