package com.farmtastic.admin.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.admin.model.Admin;
import com.farmtastic.admin.model.AdminFunction;
import com.farmtastic.admin.model.AdminService;
import com.farmtastic.admin.model.AdminType;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminManagementController {

	@Autowired
	private AdminService adminService;

    // 定義這個 Controller 下所有功能所需要的共同權限
    private static final String REQUIRED_PERMISSION = "網站總管理";

    /**
     * 檢查使用者是否未登入或權限不足的輔助方法。
     * @param session HttpSession
     * @param requiredPermission 需要的權限名稱
     * @return 如果未登入或權限不足則返回 true
     */
    private boolean permissionDenied(HttpSession session, String requiredPermission) {
        Object userObject = session.getAttribute("LOGGED_IN_ADMIN");
        // 1. 檢查是否登入
        if (userObject == null || !(userObject instanceof Admin)) {
            return true;
        }
        Admin loggedInAdmin = (Admin) userObject;
        // 2. 檢查是否有角色或權限設定
        if (loggedInAdmin.getAdminType() == null || loggedInAdmin.getAdminType().getFunctions() == null) {
            return true; // 沒有角色就視為沒有權限
        }
        // 3. 提取使用者擁有的權限名稱
        Set<String> permissions = loggedInAdmin.getAdminType().getFunctions().stream()
                .map(AdminFunction::getAdminFuncName)
                .collect(Collectors.toSet());
        // 4. 判斷是否包含所需權限
        return !permissions.contains(requiredPermission);
    }

    /**
     * 處理權限不足時的重新導向。
     * @param redirectAttributes 用於傳遞快閃訊息
     * @return 重新導向到主管理頁面的路徑
     */
    private String handleNoPermission(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "您的權限不足，無法存取此功能！");
        return "redirect:/managepage";
    }

	private void loadCommonData(Model model) {
		List<AdminType> adminTypes = adminService.findAllAdminTypes();
		model.addAttribute("allAdminTypes", adminTypes);
	}

	// 網站總管理主頁
	@GetMapping("/management")
	public String managementIndex(HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
		return "back_end/logined/admin/admin/admin_management";
	}

	// 管理員列表頁面
	@GetMapping("/list")
	public String showAdminList(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
		List<Admin> adminList = adminService.findAllAdmins();
		model.addAttribute("admins", adminList);
		return "back_end/logined/admin/admin/admin_list";
	}

	// 顯示「新增」管理員的表單
	@GetMapping("/add")
	public String showAddForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
		model.addAttribute("admin", new Admin());
		model.addAttribute("pageTitle", "新增管理員");
		loadCommonData(model);
		return "back_end/logined/admin/admin/admin_form";
	}

	// 顯示「修改」管理員的表單
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpSession session) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
		Optional<Admin> adminOptional = adminService.findById(id);
		if (adminOptional.isPresent()) {
			model.addAttribute("admin", adminOptional.get());
			model.addAttribute("pageTitle", "修改管理員");
			loadCommonData(model);
			return "back_end/logined/admin/admin/admin_form";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "找不到該管理員！");
			return "redirect:/admin/list";
		}
	}

	// 處理來自新增或修改表單的 POST 請求
    @PostMapping("/save")
	public String saveAdmin(@ModelAttribute("admin") Admin admin, RedirectAttributes redirectAttributes,
			HttpSession session) {
		if (permissionDenied(session, REQUIRED_PERMISSION))
			return handleNoPermission(redirectAttributes);
		// 判斷是「修改」還是「新增」
		if (admin.getAdminId() != null) { // 修改模式
			// 從資料庫撈取舊資料
			Optional<Admin> existingAdminOptional = adminService.findById(admin.getAdminId());
			if (existingAdminOptional.isPresent()) {
				Admin existingAdmin = existingAdminOptional.get();
				// 檢查前端傳來的密碼是否為空
				if (!StringUtils.hasText(admin.getAdminPwd())) {
					// 如果是空的，就用舊的密碼覆蓋，避免密碼被清空
					admin.setAdminPwd(existingAdmin.getAdminPwd());
				}
				// 將舊的管理員類型設定回去，因為表單上沒有這個欄位
				admin.setAdminType(existingAdmin.getAdminType());
			}
		} else { // 新增模式
			// 在實際應用中，這裡應該對新密碼進行加密
			// 並且應該設定一個預設的管理員類型
			// 這裡我們暫時不做設定，依賴 Service 層的處理
		}

		adminService.save(admin);
		redirectAttributes.addFlashAttribute("successMessage", "管理員資料儲存成功！");
		return "redirect:/admin/list";
	}

	// 權限設定頁面
    @GetMapping("/permissions")
    public String showPermissionPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        
        List<AdminType>	adminTypeWithPermission = adminService.findAllAdminTypeWithFunc();
        List<AdminFunction> allFunctions = adminService.findAllFunc();
        
        model.addAttribute("adminTypes", adminTypeWithPermission);
        model.addAttribute("allFunctions", allFunctions);
        
        return "back_end/logined/admin/admin/admin_Func";
    }
    
    // 處理權限更新的 POST 請求
    @PostMapping("/permissions/update")
    public String updatePermissions(@RequestParam("typeId") Integer typeId,
                                    @RequestParam(name = "functionIds", required = false) List<Integer> functionIds,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        
        try {
            // 步驟 1：更新資料庫中的權限
            adminService.updatePermissions(typeId, functionIds);
            redirectAttributes.addFlashAttribute("successMessage", "權限更新成功！");

            // *** 核心修正：檢查並更新當前使用者的 Session ***
            Admin currentUser = (Admin) session.getAttribute("LOGGED_IN_ADMIN");
            // 檢查當前使用者是否存在，且他的角色ID是否就是剛剛被修改的那個角色ID
            if (currentUser != null && currentUser.getAdminType().getAdminTypeId().equals(typeId)) {
                // 從資料庫重新獲取最新的使用者資料 (包含更新後的權限)
                Optional<Admin> updatedAdminOptional = adminService.findById(currentUser.getAdminId());
                if (updatedAdminOptional.isPresent()) {
                    // 將最新的使用者資料存回 Session，覆蓋舊的資料
                    session.setAttribute("LOGGED_IN_ADMIN", updatedAdminOptional.get());
                    System.out.println("偵測到當前使用者權限變更，已更新 Session。");
                }
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "權限更新失敗：" + e.getMessage());
            e.printStackTrace(); // 在後台印出詳細錯誤，方便除錯
        }
        
        return "redirect:/admin/permissions";
    }
    
    //後台會員管理
    @GetMapping("/mem-management")
    public String showMemManagementPage(HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        return "back_end/logined/admin/mem/mem_management";
    }
    
    //後台低分商品管理
    @GetMapping("/pro-management")
    public String showProManagementPage(HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        return "back_end/logined/admin/pro/pro_management";
    }
}
