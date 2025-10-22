package com.farmtastic.couponlog.model;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.farmtastic.admin.model.Admin;
import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

import jakarta.servlet.http.HttpSession;

//折價券操作日誌切面 (Aspect)

@Aspect
@Component
public class CouponLogAspect {

	private final CouponLogService logService;
	private final ProCpnRepository proCpnRepo;
//	private static final String USER_SESSION_KEY = "LOGGED_IN_ADMIN";

	@Autowired
	public CouponLogAspect(CouponLogService logService, ProCpnRepository proCpnRepo) {
		this.logService = logService;
		this.proCpnRepo = proCpnRepo;
	}

	// 攔截 com.farmtastic.procpn.controller 下所有 Controller 類別的方法
	@Pointcut("execution(* com.farmtastic.procpn.controller.*.*(..))")
	public void couponControllerMethods() {
	}

	// 後置通知
	@AfterReturning("couponControllerMethods()")
	public void recordActionLog(JoinPoint joinPoint) {
		// 取得方法名稱
		String methodName = joinPoint.getSignature().getName();

		// 根據方法名稱判斷操作類型
		String actionType = parseActionType(methodName);

		// 紀錄操作描述
		String description = buildDescription(methodName, joinPoint);

		// 取得目前的 Session
		Admin admin = getLoggedInAdmin();
		Integer adminId = (admin != null) ? admin.getAdminId() : 0;
		if (admin != null) {
			System.out.println(" AOP 已成功取得 Session 管理員：" + admin.getAdminName() + " (ID=" + admin.getAdminId() + ")");
		} else {
			System.err.println(" AOP 無法取得 Session（可能未登入或非 HTTP 請求）");
		}
		// 取得折價券 ID
		Integer proCpnId = extractCouponId(joinPoint);
		// 如果 Controller 傳進來的 ID 是 null（新增），那我們自己查「最新一筆」
		if (proCpnId == null) {
			try {
				ProCpnVO latest = proCpnRepo.findTopByOrderByProCpnIdDesc();
				if (latest != null) {
					proCpnId = latest.getProCpnId();
				}
			} catch (Exception e) {
				System.err.println("無法自動取得最新折價券ID：" + e.getMessage());
			}
		}

		// 寫入日誌
		logService.addLog(adminId, proCpnId, actionType, description);

		System.out.println(" [CouponLogAspect] 已攔截操作：" + actionType + " → " + methodName);
	}

	/** 從 Session 取得登入管理員 */
	private Admin getLoggedInAdmin() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return null;

		HttpSession session = attrs.getRequest().getSession(false);
		if (session == null)
			return null;

		Object stored = session.getAttribute("LOGGED_IN_ADMIN");
		if (stored instanceof Admin admin) {
			return admin;
		}
		return null;
	}

	/** 嘗試從參數取出折價券ID */
	private Integer extractCouponId(JoinPoint joinPoint) {
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer)
				return (Integer) arg;

			if (arg instanceof ProCpnVO)
				return ((ProCpnVO) arg).getProCpnId();
			if (arg instanceof ProCpnFormDTO)
				return ((ProCpnFormDTO) arg).getProCpnId();

			// 保底：嘗試反射找 getter
			if (arg != null) {
				try {
					var method = arg.getClass().getMethod("getProCpnId");
					Object id = method.invoke(arg);
					if (id instanceof Integer)
						return (Integer) id;
				} catch (Exception ignored) {
				}
			}
		}
		return null;
	}

	// 判斷方法名稱對應的操作類型
	private String parseActionType(String methodName) {
		methodName = methodName.toLowerCase();

		if (methodName.contains("add") || methodName.contains("insert")) {
			return "新增";
		} else if (methodName.contains("update") || methodName.contains("edit")) {
			return "修改";
		} else if (methodName.contains("delete") || methodName.contains("remove")) {
			return "刪除";
		} else if (methodName.contains("toggle") || methodName.contains("change") || methodName.contains("switch")) {
			return "狀態變更";
		} else {
			return "其他操作";
		}
	}

	//
	private String buildDescription(String methodName, JoinPoint joinPoint) {
		methodName = methodName.toLowerCase();

		// 嘗試取得折價券名稱
		String cpnName = extractCouponName(joinPoint);

		if (methodName.contains("add")) {
			return "新增折價券：" + (cpnName != null ? cpnName : "（未命名）");
		} else if (methodName.contains("update") || methodName.contains("edit")) {
			return "修改折價券：" + (cpnName != null ? cpnName : "（未命名）");
		} else if (methodName.contains("delete") || methodName.contains("remove")) {
			return "刪除折價券：" + (cpnName != null ? cpnName : "（未命名）");
		} else if (methodName.contains("toggle") || methodName.contains("status")) {
			return "切換折價券狀態：" + (cpnName != null ? cpnName : "（未命名）");
		} else if (methodName.contains("list")) {
			return "檢視折價券清單";
		} else if (methodName.contains("show")) {
			return "開啟折價券表單頁面";
		} else {
			return "執行其他操作：" + methodName;
		}
	}

	/** 嘗試從參數物件取出折價券名稱 */
	private String extractCouponName(JoinPoint joinPoint) {
		for (Object arg : joinPoint.getArgs()) {
			if (arg == null)
				continue;
			if (arg instanceof Integer) {
				Integer id = (Integer) arg;
				try {
					ProCpnVO cpn = proCpnRepo.findById(id).orElse(null);
					if (cpn != null && cpn.getCpnName() != null && !cpn.getCpnName().isBlank()) {
						return cpn.getCpnName();
					}

				} catch (Exception ignored) {
				}
			}
		}
		return null;
	}
}
