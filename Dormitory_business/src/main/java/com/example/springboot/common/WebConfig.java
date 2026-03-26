package com.example.springboot.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册权限控制拦截器
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/static/**", "/css/**", "/js/**", "/images/**"); // 排除静态资源
    }
}
