package com.farmtastic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.farmtastic.member.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/", "/mem/showMemRegLoginForm", "/mem/register", "/mem/login", 
								 "/mem/forgetPasswordPage", "/mem/forgetPassword", 
								 "/mem/resetPasswordPage", "/mem/resetPassword",
								 "/mem/verifyEmail", "/mem/farmerStoreProd", "/mem/farmerStoreAct",
								 "/css/**", "/js/**", "/images/**", "/error").permitAll()
				.requestMatchers("/mem/memArea/**").authenticated()
				.anyRequest().permitAll()
			)
			// ✅ 加上這個：確保 SecurityContext 存到 Session
	        .securityContext(securityContext -> securityContext
	            .requireExplicitSave(false)  // 自動儲存 SecurityContext 到 Session
	        )
			.formLogin(form -> form.disable())
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/mem/showMemRegLoginForm")
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
				)
				.successHandler(oauth2SuccessHandler)
				.failureUrl("/mem/showMemRegLoginForm?error=true")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/mem/logout")
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true)
				.permitAll()
			)
			.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}




