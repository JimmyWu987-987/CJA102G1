package com.farmtastic.admin.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private AdminTypeRepository adminTypeRepository;
	
	@Autowired
	private AdminFuncRepository adminFuncRepository;

	// *** 修正：統一使用 LOGGED_IN_ADMIN 作為 Session Key ***
	private static final String USER_SESSION_KEY = "LOGGED_IN_ADMIN";

	/**
	 * 處理登入邏輯
	 * 
	 * @param account  帳號
	 * @param password 密碼
	 * @return 如果驗證成功，返回包含 Admin 物件的 Optional；否則返回空的 Optional
	 */
	public Optional<Admin> login(String account, String password) {
		System.out.println("========== 登入驗證開始 ==========");
		System.out.println("查詢帳號: " + account);

		Optional<Admin> adminOptional = adminRepository.findByAdminAcc(account);

		if (adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			System.out.println("找到帳號，管理員名稱: " + admin.getAdminName());
			System.out.println("密碼比對中...");

			// 檢查密碼是否相符以及帳號狀態是否為啟用 (1)
			if (admin.getAdminPwd().equals(password)) {
				System.out.println("密碼正確");

				if (admin.getAdminStatus() == 1) {
					System.out.println("帳號狀態：啟用");
					System.out.println("登入驗證成功！");
					System.out.println("========== 登入驗證結束 ==========");
					return Optional.of(admin);
				} else {
					System.out.println("帳號狀態：停用 (status=" + admin.getAdminStatus() + ")");
				}
			} else {
				System.out.println("密碼錯誤");
			}
		} else {
			System.out.println("查無此帳號");
		}

		System.out.println("登入驗證失敗");
		System.out.println("========== 登入驗證結束 ==========");
		return Optional.empty();
	}

	/**
	 * 將登入成功的使用者資訊存入 Session
	 * 
	 * @param session HttpSession 物件
	 * @param admin   登入的使用者 Admin 物件
	 */
	public void storeUserInSession(HttpSession session, Admin admin) {
		System.out.println("========== 開始儲存 Session ==========");
		System.out.println("Session ID: " + session.getId());
		System.out.println("Session Key: " + USER_SESSION_KEY);
		System.out.println("要儲存的管理員: " + admin.getAdminName() + " (帳號: " + admin.getAdminAcc() + ")");

		// 存入 Session
		session.setAttribute(USER_SESSION_KEY, admin);

		// 立即驗證是否存入成功
		Object stored = session.getAttribute(USER_SESSION_KEY);
		if (stored != null && stored instanceof Admin) {
			Admin storedAdmin = (Admin) stored;
			System.out.println("✓ Session 儲存成功！");
			System.out.println("  - 儲存的管理員: " + storedAdmin.getAdminName());
			System.out.println("  - 管理員帳號: " + storedAdmin.getAdminAcc());

			// 檢查角色資訊
			if (storedAdmin.getAdminType() != null) {
				System.out.println("  - 角色類型: " + storedAdmin.getAdminType().getAdminTypeName());
			} else {
				System.out.println("  - 警告：管理員沒有設定角色！");
			}
		} else {
			System.err.println("✗ Session 儲存失敗！stored = " + stored);
		}
		System.out.println("========== Session 儲存完成 ==========");
	}

	/**
	 * 處理登出邏輯
	 * 
	 * @param session HttpSession 物件
	 */
	public void logout(HttpSession session) {
		System.out.println("使用者登出，Session ID: " + session.getId());
		// 從 session 中移除使用者物件
		session.removeAttribute(USER_SESSION_KEY);
		// 讓 session 失效
		session.invalidate();
		System.out.println("Session 已清除");
	}

	/**
	 * 檢查當前登入的使用者是否有權限執行某項功能
	 * 
	 * @param session          HttpSession 物件
	 * @param requiredFuncName 需要檢查的功能名稱 (對應 admin_func_name)
	 * @return 如果有權限，返回 true；否則返回 false
	 */
	public boolean checkPermission(HttpSession session, String requiredFuncName) {
		Object userObject = session.getAttribute(USER_SESSION_KEY);

		// 1. 檢查使用者是否登入
		if (!(userObject instanceof Admin)) {
			System.out.println("權限檢查失敗：使用者未登入或 Session 中無資料");
			return false;
		}

		Admin loggedInAdmin = (Admin) userObject;

		// 2. 取得該使用者的角色
		AdminType adminType = loggedInAdmin.getAdminType();
		if (adminType == null || adminType.getFunctions() == null) {
			System.out.println("權限檢查失敗：使用者 " + loggedInAdmin.getAdminAcc() + " 沒有設定角色或角色沒有任何權限");
			return false;
		}

		Set<AdminFunction> functions = adminType.getFunctions();

		// 3. 遍歷該角色的所有功能，檢查是否包含需要的功能
		for (AdminFunction function : functions) {
			if (function.getAdminFuncName().equals(requiredFuncName)) {
				System.out.println("✓ 使用者 " + loggedInAdmin.getAdminAcc() + " 權限檢查通過，可使用功能：" + requiredFuncName);
				return true; // 找到對應的功能，權限通過！
			}
		}

		System.out.println("✗ 權限檢查失敗：使用者 " + loggedInAdmin.getAdminAcc() + " 沒有 " + requiredFuncName + " 的權限");
		return false; // 遍歷完所有功能都沒找到，權限不通過
	}

	public List<Admin> findAllAdmins() {
		return adminRepository.findAll();
	}

	// 新增：根據 ID 查詢管理員
	public Optional<Admin> findById(Integer id) {
		return adminRepository.findById(id);
	}

	// 新增：儲存管理員 (可用於新增和修改)
	public void save(Admin admin) {
	       if (admin.getAdminId() == null) { // 判斷為新增
	            // 如果管理員類型是空的（因為我們從表單移除了這個欄位）
	            if (admin.getAdminType() == null) {
	                // 給定一個預設的管理員類型 ID，例如 3 代表 "低級人員"
	                // 這裡的 findById 回傳的是 Optional，我們需要處理它
	                Optional<AdminType> defaultType = adminTypeRepository.findById(3);
	                // 如果找到了預設類型，就設定給新的 admin 物件
	                defaultType.ifPresent(admin::setAdminType);
	            }
	        }
	        adminRepository.save(admin);
	    }

	// 新增：提供查詢所有管理員類型的功能
	public List<AdminType> findAllAdminTypes() {
		return adminTypeRepository.findAll();
	}
	
	public List<AdminFunction>	findAllFunc(){
		return adminFuncRepository.findAll();
	}

	// 管理員權限檢查

	public List<AdminType> findAllAdminTypeWithFunc() {
		List<AdminType> types = adminTypeRepository.findAll();
		return types;
	}
	
    @Transactional
    public void updatePermissions(Integer adminTypeId, List<Integer> functionIds) {
        AdminType adminType = adminTypeRepository.findById(adminTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Admin Type Id:" + adminTypeId));

        // 如果 functionIds 是空的 (代表取消所有權限)，就清空
        if (functionIds == null || functionIds.isEmpty()) {
            adminType.getFunctions().clear();
        } else {
            // 根據傳入的 ID 列表，查詢對應的 AdminFunction 物件
            List<AdminFunction> functions = adminFuncRepository.findAllById(functionIds);
            // 將 List 轉換為 Set 並更新到 AdminType 物件上
            adminType.setFunctions(new HashSet<>(functions));
        }
        
        adminTypeRepository.save(adminType);
    }
    
   // --- 功能(AdminFunction)相關服務 ---
    
    /**
     * 查詢所有功能
     * @return 功能列表
     */
    public List<AdminFunction> findAllFunctions() {
        return adminFuncRepository.findAll();
    }

    /**
     * 根據 ID 查詢單一功能
     * @param id 功能 ID
     * @return Optional<AdminFunction>
     */
    public Optional<AdminFunction> findFunctionById(Integer id) {
        return adminFuncRepository.findById(id);
    }

    /**
     * 儲存 (新增或更新) 功能
     * @param adminFunction 要儲存的功能物件
     */
    public void saveFunction(AdminFunction adminFunction) {
    	adminFuncRepository.save(adminFunction);
    }

    /**
     * 根據 ID 刪除功能
     * @param id 功能 ID
     */
    public void deleteFunctionById(Integer id) {
        // 在實際應用中，刪除前應檢查是否有角色正在使用此功能
    	adminFuncRepository.deleteById(id);
    }
}
