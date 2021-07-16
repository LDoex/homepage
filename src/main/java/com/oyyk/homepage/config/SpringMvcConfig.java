package com.oyyk.homepage.config;

import com.oyyk.homepage.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/redis/**",
                        "/user/login",
                        "/homeCategory/all",
                        "/memberinfo/list",
                        "/doc/all/**",
                        "/doc/find-content/**",
                        "/doc/find-contents/**",
                        "/others/find-content/**",
                        "/file/**"
                );
    }
}