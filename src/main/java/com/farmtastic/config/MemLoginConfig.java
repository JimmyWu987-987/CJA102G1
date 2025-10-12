package com.farmtastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.farmtastic.member.interceptor.MemLoginInterceptor;

@Configuration
public class MemLoginConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
<<<<<<< Upstream, based on branch 'develop' of https://github.com/JimmyWu987-987/CJA102G1.git
		registry.addInterceptor(new MemLoginInterceptor())
				.addPathPatterns(
						"/mem/memArea/**", 
						"/mem/proorders/**",
						"/mem/favopro/**" ,
						"/cart/checkoutByFmemId");
=======
		registry.addInterceptor(new MemLoginInterceptor()).addPathPatterns("/mem/memArea/**", "/mem/proorders/**",
				"/cart/checkoutByFmemId");
>>>>>>> 6945c41 攔截更新
//				.excludePathPatterns(
//						"/mem/showMemRegLoginForm",
//						"/mem/forgetPasswordPage",
//						"/mem/resetPasswordPage");
	}
}

//@GetMapping("/showMemRegLoginForm")
//@PostMapping("/register")
//@GetMapping("/verifyEmail")
//@GetMapping("/forgetPasswordPage")
//@PostMapping("/forgetPassword")
//@GetMapping("/resetPasswordPage")
//@PostMapping("/resetPassword")
//@PostMapping("/login")
//	
//@PostMapping("/logout")
// @GetMapping("/toMemArea")
//@GetMapping("/memArea")
// @GetMapping("/toUpdateProfile")
//@GetMapping("/memArea/updateProfilePage")
//@PostMapping("/memArea/updateProfile")
// @GetMapping("/toUpdatePassword")
//@GetMapping("/memArea/updatePasswordPage")
//@PostMapping("/memArea/updatePassword")