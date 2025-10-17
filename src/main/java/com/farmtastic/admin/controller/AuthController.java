package com.farmtastic.admin.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.admin.model.Admin;
import com.farmtastic.admin.model.AdminService;
import com.farmtastic.admin.model.LoginRequest;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    // 這裡的參數 LoginRequest loginRequest 會自動從表單的 name="account", name="password" 綁定
    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("========== 登入流程開始 ==========");
        System.out.println("接收到的帳號: " + loginRequest.getAccount());
        System.out.println("Session ID: " + session.getId());
        try {
            Optional<Admin> adminOptional = adminService.login(loginRequest.getAccount(), loginRequest.getPassword());

            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();
                System.out.println("登入驗證成功，使用者: " + admin.getAdminName());
                
                adminService.storeUserInSession(session, admin);
                
                // 驗證 Session 中是否真的存入了資料
                Object storedAdmin = session.getAttribute("LOGGED_IN_ADMIN");
                System.out.println("Session 中儲存的管理員: " + storedAdmin);
                System.out.println("準備重導向至 /managepage");
                System.out.println("========== 登入流程結束 ==========");
                
                return "redirect:/managepage"; 
            } else {
                System.out.println("登入驗證失敗：帳號或密碼錯誤");
                redirectAttributes.addFlashAttribute("loginError", "帳號或密碼錯誤，或帳號已被停用。");
                return "redirect:/admin/login"; 
            }
        } catch (Exception e) {
            System.err.println("登入時發生內部錯誤: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("loginError", "伺服器發生未知錯誤，請聯繫系統管理員。");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        adminService.logout(session);
        return "redirect:/admin/login";
    }
}
