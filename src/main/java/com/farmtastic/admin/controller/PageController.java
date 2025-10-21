package com.farmtastic.admin.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.admin.model.Admin;
import com.farmtastic.admin.model.AdminFunction;
import com.farmtastic.procom.model.ProComService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private ProComService proComService;
	
    // 登入頁面路徑
    @GetMapping("/admin/login")
    public String loginPage() {
        // *** 重要：請確保你的登入頁面 html 檔案叫做 index.html ***
        return "back_end/unlogined/adminindex";
    }

//    // 根目錄重新導向到登入頁
//    @GetMapping("/")
//    public String rootRedirect() {
//        return "redirect:/admin/login";
//    }

    // 管理儀表板頁面
    @GetMapping("/managepage")
    public String dashboard(HttpSession session, Model model) {
        Object userObject = session.getAttribute("LOGGED_IN_ADMIN");

        if (userObject == null || !(userObject instanceof Admin)) {
            // 如果未登入，導向到新的登入頁
            return "redirect:/admin/login";
        }

        Admin loggedInAdmin = (Admin) userObject;

        // 取得該使用者擁有的所有權限名稱 Set
        Set<String> permissions = loggedInAdmin.getAdminType() != null ?
                loggedInAdmin.getAdminType().getFunctions().stream()
                        .map(AdminFunction::getAdminFuncName)
                        .collect(Collectors.toSet())
                : Collections.emptySet();
        
        // 新增：查詢低評分商品數量=================
        long lowRatedCount = 0;
        try {
            lowRatedCount = proComService.countActiveLowRatedProducts();
        } catch (Exception e) {
            // 如果查詢失敗，記錄錯誤但不影響頁面顯示
            System.err.println("查詢低評分商品數量失敗: " + e.getMessage());
        }
        
        
        // 建立一個按鈕與所需權限的對應 Map
        Map<String, String[]> managementButtons = new LinkedHashMap<>();
        // 按鈕顯示文字, [連結路徑, 所需權限名稱]
        managementButtons.put("商城管理",   new String[]{"/admin/pro-management",    "商城管理"});
        managementButtons.put("活動管理",   new String[]{"#",                 "活動管理"}); 
        managementButtons.put("廣告管理",    new String[]{"/admin/proAd/list","廣告管理"}); 
        managementButtons.put("金流管理",   new String[]{"/admin/cashflow/",     "金流管理"}); 
        managementButtons.put("會員管理",   new String[]{"/admin/mem-management","會員管理"}); 
        managementButtons.put("折價券管理", new String[]{"/admin/coupon/manage","折價券管理"});
        managementButtons.put("最新消息管理",    new String[]{"/news",          "最新消息管理"});
        managementButtons.put("QA 管理",    new String[]{"/qa/list",          "QA管理"});


        model.addAttribute("adminName", loggedInAdmin.getAdminName());
        model.addAttribute("permissions", permissions);
        model.addAttribute("managementButtons", managementButtons); // 將按鈕對應表傳給前端
        model.addAttribute("lowRatedProductCount", lowRatedCount);

        return "back_end/logined/admin/admin/managepage"; 
    }
}

