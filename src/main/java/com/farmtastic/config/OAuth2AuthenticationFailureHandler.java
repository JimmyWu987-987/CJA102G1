package com.farmtastic.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage = "Google 登入失敗";
        
        // 取得具體錯誤訊息
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauthException = (OAuth2AuthenticationException) exception;
            errorMessage = oauthException.getError().getDescription();
            
            // 如果沒有 description，使用預設訊息
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = "Google 登入失敗，請稍後再試";
            }
        }
        
        System.out.println("OAuth2 登入失敗: " + errorMessage);
        
        // URL 編碼錯誤訊息（處理中文）
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        
        // 重導到登入頁，並帶上錯誤訊息
        String redirectUrl = "/mem/showMemRegLoginForm?googleError=" + encodedError;
        
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}