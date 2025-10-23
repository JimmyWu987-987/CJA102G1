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
	public String saveAdmin(@ModelAttribute("admin") Admin admin, 
	                        RedirectAttributes redirectAttributes,
	                        Model model,
	                        HttpSession session) {
	    if (permissionDenied(session, REQUIRED_PERMISSION))
	        return handleNoPermission(redirectAttributes);

	    // *** 核心修正：加入後端驗證 ***
	    boolean isNew = admin.getAdminId() == null;

	    // 1. 驗證帳號
	    if (!StringUtils.hasText(admin.getAdminAcc())) {
	        model.addAttribute("errorMessage", "儲存失敗：帳號為必填欄位！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 2. 驗證姓名
	    if (!StringUtils.hasText(admin.getAdminName())) {
	        model.addAttribute("errorMessage", "儲存失敗：姓名為必填欄位！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 3. 驗證密碼（新增時必填）
	    if (isNew && !StringUtils.hasText(admin.getAdminPwd())) {
	        model.addAttribute("errorMessage", "儲存失敗：新增管理員時，密碼為必填欄位！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", "新增管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 4. 驗證管理員類型
	    if (admin.getAdminType() == null || admin.getAdminType().getAdminTypeId() == null) {
	        model.addAttribute("errorMessage", "儲存失敗：必須為管理員指派一個角色！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 5. *** 新增：驗證狀態（必須選擇啟用或停用）***
	    if (admin.getAdminStatus() == null) {
	        model.addAttribute("errorMessage", "儲存失敗：必須選擇管理員狀態（啟用或停用）！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 6. 驗證 Email 格式（如果有填寫）
	    if (!StringUtils.hasText(admin.getAdminEmail())) {
	        model.addAttribute("errorMessage", "儲存失敗：Email 為必填欄位！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	    if (!admin.getAdminEmail().matches(emailRegex)) {
	        model.addAttribute("errorMessage", "儲存失敗：Email 格式不正確！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }

	    // 7. 驗證手機（必填且格式正確）
	    if (!StringUtils.hasText(admin.getAdminMobile())) {
	        model.addAttribute("errorMessage", "儲存失敗：手機號碼為必填欄位！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }
	    String mobileRegex = "^09\\d{8}$";
	    if (!admin.getAdminMobile().matches(mobileRegex)) {
	        model.addAttribute("errorMessage", "儲存失敗：手機號碼格式不正確（應為09開頭的10位數字）！");
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }
	    
	    // *** 驗證結束 ***
	    
	    // 如果是修改且未填寫密碼，保留原密碼
	    if (!isNew) {
	        Optional<Admin> existingAdminOptional = adminService.findById(admin.getAdminId());
	        if (existingAdminOptional.isPresent()) {
	            Admin existingAdmin = existingAdminOptional.get();
	            if (!StringUtils.hasText(admin.getAdminPwd())) {
	                admin.setAdminPwd(existingAdmin.getAdminPwd());
	            }
	        }
	    }

	    try {
	        adminService.save(admin);
	        redirectAttributes.addFlashAttribute("successMessage", "管理員資料儲存成功！");
	        return "redirect:/admin/list";
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "儲存失敗：" + e.getMessage());
	        model.addAttribute("admin", admin);
	        model.addAttribute("pageTitle", isNew ? "新增管理員" : "修改管理員");
	        loadCommonData(model);
	        return "back_end/logined/admin/admin/admin_form";
	    }
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
    
    // 處理刪除請求的方法
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        try {
            adminService.deleteAdminById(id);
            redirectAttributes.addFlashAttribute("successMessage", "管理員 " + id + " 刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/admin/list";
    }
    
    
    //後台會員管理
    @GetMapping("/mem-management")
    public String showMemManagementPage(HttpSession session, RedirectAttributes redirectAttributes) {
		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
        return "back_end/logined/admin/mem/mem_management";
    }
    
//    //後台低分商品管理
//    @GetMapping("/pro-management")
//    public String showProManagementPage(HttpSession session, RedirectAttributes redirectAttributes) {
//		if (permissionDenied(session, REQUIRED_PERMISSION)) return handleNoPermission(redirectAttributes);
//        return "back_end/logined/admin/pro/pro_management";
//    }
}
