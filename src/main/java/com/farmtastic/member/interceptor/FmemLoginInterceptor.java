package com.farmtastic.member.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class FmemLoginInterceptor implements HandlerInterceptor{
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("loggedInFmember") == null) {
			
//			String uri = request.getRequestURI();
//			String queryString = request.getQueryString();
//			String fullUrl = uri + (queryString != null ? "?"+queryString : "");
//			
//			session.setAttribute("redirectAfterLogin", fullUrl);
			
			response.sendRedirect(request.getContextPath() + "/fmem/showFmemRegLoginForm");
			return false;
		}

		return true;
	}
}


