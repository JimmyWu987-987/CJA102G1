package com.farmtastic.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private MemRepository memRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		String email = oauth2User.getAttribute("email");

		// 從資料庫取得完整的會員資料
		Mem mem = memRepository.findByMemEmail(email);
		if(mem == null) {
			throw new RuntimeException("使用者不存在");
		}

		// 將會員資料存入 Session（與你原本的登入方式一致）
		HttpSession session = request.getSession();
		session.setAttribute("loggedInMember", mem);
		session.setAttribute("memId", mem.getMemId());
		
		// *檢查是否需要補充生日
	    if (mem.getMemBirthday() == null) {
	        getRedirectStrategy().sendRedirect(request, response, "/mem/memArea/completeProfilePage");
	        return;
	    }

		// 檢查是否有重導 URL
		String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
		if (redirectUrl != null) {
			session.removeAttribute("redirectAfterLogin");
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		} else {
			// 預設導向會員專區
			getRedirectStrategy().sendRedirect(request, response, "/mem/memArea");
		}
	}
}