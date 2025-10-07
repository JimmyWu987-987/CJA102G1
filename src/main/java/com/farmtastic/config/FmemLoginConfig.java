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
				.addPathPatterns("/fmem/fmemArea/**", "/fmem/home");
//				.excludePathPatterns(
//						"/fmem/register",
//						"/fmem/login",
//						"/fmem/showFmemRegLoginForm"
//						"/fmem/forgetPasswordPage",
//						"/fmem/resetPasswordPage"
//				);
	}
}


//@GetMapping("/showFmemRegLoginForm")
//@PostMapping("/register")
//@PostMapping("/login")
//	
//@PostMapping("/logout")	
//@GetMapping("/home")
					//@GetMapping("/toFmemArea")
//@GetMapping("/fmemArea")
					//@GetMapping("/toUpdateProfile")
//@GetMapping("/fmemArea/updateProfilePage")
//@PostMapping("/fmemArea/updateProfile")
					//@GetMapping("/toUpdatePassword")
//@GetMapping("/fmemArea/updatePasswordPage")
//@PostMapping("/fmemArea/updatePassword")
					//@GetMapping("/toUpdateStore")
//@GetMapping("/fmemArea/updateStorePage")
//@PostMapping("/fmemArea/updateStore")