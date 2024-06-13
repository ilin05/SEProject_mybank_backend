package com.mybank.module1_counter.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin/*")
//                .excludePathPatterns("/admin/login");
//        registry.addInterceptor(new CashierInterceptor()).addPathPatterns("/cashier/*")
//                .excludePathPatterns("/cashier/login");
    }
}
