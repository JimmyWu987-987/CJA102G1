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
			response.sendRedirect(request.getContextPath() + "/fmem/showFmemRegLoginForm");
			return false;
		}
		return true;
	}
}


