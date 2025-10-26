package com.farmtastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.farmtastic.member.interceptor.MemLoginInterceptor;

@Configuration
public class MemLoginConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MemLoginInterceptor()).addPathPatterns("/mem/memArea/**", "/mem/proorders/**",
				"/mem/favo/**", "/mem/memcpn/**", "/cart/checkoutByFmemId")
				.excludePathPatterns(
						"/mem/showMemRegLoginForm",
						"/mem/forgetPasswordPage",
						"/mem/resetPasswordPage",
						"/mem/register", 
						"/mem/login", 
						"/mem/verifyEmail");
	}
}

