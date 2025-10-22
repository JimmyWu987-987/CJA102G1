package com.farmtastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.farmtastic.member.interceptor.FmemLoginInterceptor;

@Configuration
public class FmemLoginConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new FmemLoginInterceptor())
				.addPathPatterns("/fmem/fmemArea/**", "/fmem/home")
				.excludePathPatterns(
						"/fmem/showFmemRegLoginForm",
						"/fmem/register",
						"/fmem/login",
						"/fmem/forgetPasswordPage",
						"/fmem/forgetPassword",
						"/fmem/resetPasswordPage",
						"/fmem/resetPassword",
						"/fmem/supplementIdentityCheckPage",
						"/fmem/requestSupplement",
						"/fmem/fmemSupplementFormPage",
						"/fmem/submitSupplement",
						"/fmem/loginVerifyPage",
						"/fmem/loginVerify"
				);
	}
}

