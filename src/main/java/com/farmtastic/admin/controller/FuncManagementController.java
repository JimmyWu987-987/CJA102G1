package com.farmtastic.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.admin.model.Admin;
import com.farmtastic.admin.model.AdminFunction;
import com.farmtastic.admin.model.AdminService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/func")
public class FuncManagementController {

    @Autowired
    private AdminService adminService;
    
    // 定義這個 Controller 需要的權限名稱
    private static final String REQUIRED_PERMISSION = "網站總管理";

    /**
     * 檢查使用者是否擁有特定權限的輔助方法。
     * @param session HttpSession
     * @param requiredPermission 需要的權限名稱
     * @return 如果有權限則返回 true
     */
    private boolean hasPermission(HttpSession session, String requiredPermission) {
        Object userObject = session.getAttribute("LOGGED_IN_ADMIN");
        if (userObject == null || !(userObject instanceof Admin)) {
            return false;
        }
        Admin loggedInAdmin = (Admin) userObject;
        if (loggedInAdmin.getAdminType() == null || loggedInAdmin.getAdminType().getFunctions() == null) {
            return false;
        }
        Set<String> permissions = loggedInAdmin.getAdminType().getFunctions().stream()
                .map(AdminFunction::getAdminFuncName)
                .collect(Collectors.toSet());
        return permissions.contains(requiredPermission);
    }
    
    private String handleNoPermission(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "權限不足，無法存取此功能！");
        return "redirect:/managepage";
    }

    /**
     * 顯示所有功能的列表頁面
     */
    @GetMapping("/list")
    public String showFunctionList(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!hasPermission(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        
        List<AdminFunction> functionList = adminService.findAllFunctions();
        model.addAttribute("functions", functionList);
        return "back_end/logined/admin/admin/func_list";
    }

    /**
     * 顯示「新增」功能的表單
     */
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!hasPermission(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);

        model.addAttribute("function", new AdminFunction());
        model.addAttribute("pageTitle", "新增功能");
        return "back_end/logined/admin/admin/func_form";
    }

    /**
     * 顯示「修改」功能的表單
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!hasPermission(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);

        return adminService.findFunctionById(id)
                .map(func -> {
                    model.addAttribute("function", func);
                    model.addAttribute("pageTitle", "修改功能");
                    return "back_end/logined/admin/admin/function_form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "找不到該功能 (ID: " + id + ")");
                    return "redirect:/func/list";
                });
    }

    /**
     * 處理來自新增或修改表單的 POST 請求
     */
    @PostMapping("/save")
    public String saveFunction(@ModelAttribute("function") AdminFunction function, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!hasPermission(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);

        adminService.saveFunction(function);
        redirectAttributes.addFlashAttribute("successMessage", "功能資料儲存成功！");
        return "redirect:/func/list";
    }

    /**
     * 處理刪除功能的請求
     */
    @GetMapping("/delete/{id}")
    public String deleteFunction(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!hasPermission(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);

        try {
            adminService.deleteFunctionById(id);
            redirectAttributes.addFlashAttribute("successMessage", "功能 (ID: " + id + ") 已成功刪除。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗！可能有關聯的資料正在使用此功能。");
        }
        return "redirect:/func/list";
    }
}