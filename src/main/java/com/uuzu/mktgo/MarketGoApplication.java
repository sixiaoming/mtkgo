/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.cloud.client.SpringCloudApplication;
// import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
