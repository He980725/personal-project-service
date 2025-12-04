package com.HeZhizhu.PersonalServer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 开启 Spring Security 配置（Spring Boot 4.x 需显式添加）
public class SecurityConfig {

    // 注入 BCrypt 加密工具，全局可通过 @Autowired 使用
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 自动加密，无需额外配置
        return new BCryptPasswordEncoder();
    }
    // 核心：配置接口访问规则（放行所有/指定接口）
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF 防护（开发环境无需开启，否则 Post/Put 请求可能被拦截）
                .csrf(csrf -> csrf.disable())
                // 配置接口授权规则
                .authorizeHttpRequests(auth -> auth
                                // 方案 1：放行所有接口（开发环境最方便，推荐）
                                .anyRequest().permitAll() // 所有请求都允许匿名访问，无需认证

                        // 方案 2：只放行指定接口（更严谨，按需选择）
                        // .requestMatchers("/user/createNewUserAccount", "/user/list").permitAll() // 放行创建用户、查询用户接口
                        // .anyRequest().authenticated() // 其他接口需要认证
                );

        return http.build();
    }
}