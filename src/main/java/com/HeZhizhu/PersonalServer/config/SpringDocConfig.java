package com.HeZhizhu.PersonalServer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc 配置：优化 OpenAPI 文档信息
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 文档基本信息
                .info(new Info()
                        .title("个人后端项目接口文档") // 文档标题
                        .version("1.0.0") // 接口版本
                        .description("基于 Spring Boot 4.x 的前后端分离项目接口文档，包含用户管理、日志记录等模块") // 文档描述
                        // 作者信息（可选）
                        .contact(new Contact()
                                .name("何之柱")
                                .email("745742200@qq.com")));
    }
}