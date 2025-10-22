package com.farmtastic.couponlog.model;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.farmtastic.admin.model.Admin;

import jakarta.servlet.http.HttpSession;

//折價券操作日誌切面 (Aspect)

@Aspect
@Component
public class CouponLogAspect {

	private final CouponLogService logService;
	private static final String USER_SESSION_KEY = "LOGGED_IN_ADMIN";

	@Autowired
	public CouponLogAspect(CouponLogService logService) {
		this.logService = logService;
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
		String description = "執行方法：" + methodName + " 時間：" + LocalDateTime.now();

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
		} else {
			return "其他操作";
		}
	}
}
