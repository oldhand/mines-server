package com.mines.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * HTTP请求拦截器：记录所有请求的详细信息
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    // 线程安全的日期格式化器（可读格式：年月日 时分秒.毫秒）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("mm:ss");

    // 参数/响应内容最大长度（避免单行过长）
    private static final int MAX_CONTENT_LENGTH = 1000;

    // 记录请求开始时间（用于计算耗时）
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 请求处理前执行：记录请求基本信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录开始时间
        startTime.set(System.currentTimeMillis());

        // 包装请求，支持重复读取body（解决POST参数只能读一次的问题）
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // 打印请求基本信息
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("[Q]")
                .append("[").append(wrappedRequest.getRequestURL()).append("]")
                .append("[").append(wrappedRequest.getMethod()).append("]")
                .append(":").append(getTruncatedContent(getRequestParams(wrappedRequest)));

        log.info(requestLog.toString());
        return true; // 放行请求
    }
    /**
     * 截断过长内容（避免单行日志过长）
     */
    private String getTruncatedContent(String content) {
        // 移除换行符，确保单行显示
        String processed = content.replaceAll("\\r|\\n", " ");
        if (processed.length() <= MAX_CONTENT_LENGTH) {
            return processed;
        }
        return processed.substring(0, MAX_CONTENT_LENGTH) + "...[截断]";
    }


    /**
     * 请求处理后执行：记录响应信息和耗时
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 计算请求耗时
        long costTime = System.currentTimeMillis() - startTime.get();
        startTime.remove(); // 清除线程变量

        // 包装响应，获取响应内容
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 打印响应信息
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("[R]")
                .append("[").append(wrappedResponse.getStatus()).append("]")
                .append("[").append(getTruncatedContent(getResponseContent(wrappedResponse))).append("]")
                .append("[").append(costTime).append("ms").append("]");
        log.info(responseLog.toString());

        // 重要：将响应内容写回原始响应流（否则客户端无法收到响应）
        wrappedResponse.copyBodyToResponse();
    }

    /**
     * 获取客户端真实IP（兼容代理场景）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理时，取第一个非unknown的IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取请求参数（GET从query string，POST从body）
     */
    private String getRequestParams(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            // GET请求参数：从query string获取
            String queryString = request.getQueryString();
            return queryString == null ? "无" : queryString;
        } else if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            // POST/PUT参数：从body获取（需配合ContentCachingRequestWrapper）
            byte[] body = request.getContentAsByteArray();
            if (body.length > 0) {
                return new String(body, StandardCharsets.UTF_8);
            }
        }
        return "无";
    }

    /**
     * 获取响应内容
     */
    private String getResponseContent(ContentCachingResponseWrapper response) {
        byte[] body = response.getContentAsByteArray();
        if (body.length > 0) {
            return new String(body, StandardCharsets.UTF_8);
        }
        return "无";
    }
}
