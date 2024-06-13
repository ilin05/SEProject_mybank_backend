//package com.example.loan.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/users/login", "/public/**").permitAll() // 允许未认证的用户访问
//                                .requestMatchers("/admin/**").hasRole("ADMIN") // 只有管理员可以访问/admin/**
//                                .anyRequest().authenticated() // 其他请求需要认证
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login") // 自定义登录页面
//                                .permitAll() // 登录页面允许所有人访问
//                )
//                .logout(logout ->
//                        logout
//                                .permitAll() // 注销允许所有人访问
//                )
//                .sessionManagement(sessionManagement ->
//                        sessionManagement
//                                .maximumSessions(1) // 每个用户最多一个会话
//                                .expiredUrl("/login?expired") // 会话过期后重定向到登录页面
//                );
//        return http.build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // 密码加密器
//    }
//}

package com.mybank.module3_loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll() // 允许所有请求访问
                )
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF 保护
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 自定义登录页面
                                .permitAll() // 登录页面允许所有人访问
                )
                .logout(logout ->
                        logout
                                .permitAll() // 注销允许所有人访问
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .maximumSessions(1) // 每个用户最多一个会话
                                .expiredUrl("/login?expired") // 会话过期后重定向到登录页面
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 密码加密器
    }

    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 允许所有的源
        config.addAllowedHeader("*"); // 允许所有的头
        config.addAllowedMethod("*"); // 允许所有的方法（GET, POST, PUT, DELETE, OPTIONS等）
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}


