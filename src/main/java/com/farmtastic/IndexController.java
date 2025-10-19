package com.farmtastic;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String indexTest(HttpSession session) {
		session.removeAttribute("tempPic");
		return "/front_end/index";
	}
	
	@GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        model.addAttribute("name", oauth2User.getAttribute("name"));
        model.addAttribute("email", oauth2User.getAttribute("email"));
        return "/front_end/index"; //登入後頁面
    }
	
	
	@GetMapping("/ghome")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            String name = principal.getAttribute("name");
            String email = principal.getAttribute("email");
//            String picture = principal.getAttribute("picture");
            
            model.addAttribute("name", name);
            model.addAttribute("email", email);
//            model.addAttribute("picture", picture);
        }
        return "/front_end/index";
    }

}








