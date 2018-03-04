package com.uuzu.mktgo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.jthink.spring.boot.starter.hbase.api.HbaseTemplate;

/**
 * @author zhoujin
 */
@Service
@Slf4j
public class HbaseService {

    @Autowired
    private HbaseTemplate          hbaseTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Map<String, String> getResultByHbase(Map<String, String> rowkeys, String field, String tableName) throws InterruptedException, ExecutionException {
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);

        Map<String, String> result = new HashMap<>();
        for (String fieldkey : rowkeys.keySet()) {
            completionService.submit(new Task(fieldkey, rowkeys.get(fieldkey), field, tableName));
        }

        for (int i = 0; i < rowkeys.size(); i++) {
            Future<String> future = completionService.take();
            String[] ss = future.get().split("@");
            result.put(ss[0], ss[1]);
        }

        return result;
    }

    class Task implements Callable {

        private String key;
        private String value;
        private String field;
        private String tableName;

        public Task(String key, String value, String field, String tableName) {
            this.key = key;
            this.value = value;
            this.field = field;
            this.tableName = tableName;
        }

        @Override
        public String call() {
            try {// persona_summary//overview_all
                String count = hbaseTemplate.get(tableName, value, (result1, arg1) -> {
                    if (!result1.isEmpty()) {
                        return Bytes.toString(result1.getValue("cf".getBytes(), field.getBytes()));
                    }
                    return null;
                });
                return key + "@" + count;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;

        }
    }

    public Map<String, String> getResultByHbaseTable(Map<String, String> rowkeys, String field, String tableName) throws InterruptedException, ExecutionException {
        CompletionService completionService = new ExecutorCompletionService(threadPoolTaskExecutor);

        Map<String, String> result = new HashMap<>();
        for (String fieldkey : rowkeys.keySet()) {
            completionService.submit(new HbaseTask(fieldkey, rowkeys.get(fieldkey), field, tableName));
        }

        for (int i = 0; i < rowkeys.size(); i++) {
            Future<String> future = completionService.take();
            String[] ss = future.get().split("@");
            result.put(ss[0], ss[1]);
        }

        return result;
    }

    class HbaseTask implements Callable {

        private String key;
        private String value;
        private String field;
        private String tableName;

        public HbaseTask(String key, String value, String field, String tableName) {
            this.key = key;
            this.value = value;
            this.field = field;
            this.tableName = tableName;
        }

        @Override
        public String call() {
            try {
                String count = hbaseTemplate.get(tableName, value, (result1, arg1) -> {
                    if (!result1.isEmpty()) {
                        return Bytes.toString(result1.getValue("cf".getBytes(), field.getBytes()));
                    }
                    return null;
                });
                return key + "@" + count;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

}
