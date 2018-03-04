/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.web;

import java.util.Map;

import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 长轮询接口demo,先访问longPolling接口，再访问returnLongPollingValue
 * 
 * @author zj_pc
 */
@RestController
@RequestMapping("async")
public class AsyncController {

    private final Map<Object, DeferredResult<String>> deferredResultMap = new ConcurrentReferenceHashMap<>();

    @GetMapping("/longPolling")
    public DeferredResult<String> longPolling() {
        DeferredResult<String> deferredResult = new DeferredResult<>(0L);
        deferredResultMap.put(deferredResult.hashCode(), deferredResult);

        deferredResult.onCompletion(() -> {
            deferredResultMap.remove(deferredResult.hashCode());
            System.err.println("还剩" + deferredResultMap.size() + "个deferredResult未响应");
        });
        return deferredResult;
    }

    @GetMapping("/returnLongPollingValue")
    public void returnLongPollingValue() {
        for (Map.Entry<Object, DeferredResult<String>> entry : deferredResultMap.entrySet()) {
            entry.getValue().setResult("kl");
        }
    }
}
