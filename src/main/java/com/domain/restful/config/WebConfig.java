package com.domain.restful.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.domain.restful.handler.interceptor.LogInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Autowired
  private LogInterceptor myInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(myInterceptor).addPathPatterns("/api/product/**");
  }
}
