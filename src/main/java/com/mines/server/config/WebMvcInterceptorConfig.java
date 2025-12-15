package com.mines.server.config;

import com.mines.server.interceptor.RequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置：注册请求拦截器
 */
@Configuration
public class WebMvcInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，拦截所有请求（可根据需要排除静态资源或特定路径）
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/swagger-ui/**", // 排除Swagger UI路径
                        "/v3/api-docs/**", // 排除Swagger接口文档路径
                        "/webjars/**" // 排除静态资源
                );
    }
}
