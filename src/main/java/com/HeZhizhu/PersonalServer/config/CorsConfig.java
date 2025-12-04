package com.HeZhizhu.PersonalServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置：前后端分离架构下，前端访问后端接口需解决跨域问题
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有接口都支持跨域
                .allowedOrigins("*")  // 允许所有前端域名（开发环境使用，生产环境指定具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方法
                .allowedHeaders("*")  // 允许的请求头
                .maxAge(3600);  // 预检请求缓存时间（1小时）
    }
}