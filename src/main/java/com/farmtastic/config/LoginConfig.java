package com.farmtastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.farmtastic.member.interceptor.LoginInterceptor;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/mem/memArea");
//				.excludePathPatterns("/mem/showMemRegLoginForm");
	}
}
