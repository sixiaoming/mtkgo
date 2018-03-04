package com.uuzu.mktgo.aop;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 日志会通过消息队列来异步处理
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    private static final ObjectMapper  OBJECT_MAPPER = new ObjectMapper();

    private static ThreadLocal<Long>   startTime     = new ThreadLocal<>();
    private static ThreadLocal<String> apiName       = new ThreadLocal<>();
    private static ThreadLocal<String> strategyId    = new ThreadLocal<>();

    @Pointcut("execution(public * com.uuzu.mktgo.web.*.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        startTime.set(System.currentTimeMillis());
        Map<String, Object> requestMessage = new LinkedHashMap<>();
        String controllName = joinPoint.getSignature().getDeclaringTypeName() + "." + (joinPoint.getSignature().getName());
        Object[] par = joinPoint.getArgs();
        requestMessage.put("url", request.getRequestURL().toString());
        requestMessage.put("method", request.getMethod());
        requestMessage.put("ipaddress", request.getRemoteAddr());
        requestMessage.put("controller", controllName);
        // requestMessage.put("paremeter", par);
        apiName.set(controllName);
        log.info(OBJECT_MAPPER.writeValueAsString(requestMessage));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        Map<String, Object> responseMessage = new LinkedHashMap<>();
        responseMessage.put("controller", apiName.get());
        responseMessage.put("strategyId", strategyId.get());
        responseMessage.put("responseParemeter", ret);
        responseMessage.put("executionTime", System.currentTimeMillis() - startTime.get());
        log.info(OBJECT_MAPPER.writeValueAsString(responseMessage));

        startTime.remove();
        apiName.remove();
        strategyId.remove();
    }
}
