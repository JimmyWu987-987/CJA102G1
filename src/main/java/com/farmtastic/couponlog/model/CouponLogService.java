package com.farmtastic.couponlog.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponLogService {

	private final CouponLogRepository logRepo;

	@Autowired
	public CouponLogService(CouponLogRepository logRepo) {
		this.logRepo = logRepo;
	}

//  新增一筆折價券操作日誌紀錄
	public void addLog(Integer adminId, Integer proCpnId, String actionType, String description) {
		CouponLogVO log = new CouponLogVO();

		log.setAdminId(adminId);
		log.setProCpnId(proCpnId);
		log.setActionType(actionType);
		log.setDescription(description);
		log.setActionTime(LocalDateTime.now());

		logRepo.save(log); // 寫入資料庫
		System.out.println("已記錄操作日誌：" + actionType + " by 管理員 " + adminId);
	}

	// 查詢全部折價券操作紀錄（依時間降冪）
	public List<CouponLogVO> findAllLogs() {
		return logRepo.findAllOrderByTimeDesc();
	}

	// 查詢指定管理員的所有操作紀錄
	public List<CouponLogVO> findLogsByAdmin(Integer adminId) {
		return logRepo.findByAdminId(adminId);
	}

	// 查詢指定折價券的所有操作紀錄
	public List<CouponLogVO> findLogsByCoupon(Integer proCpnId) {
		return logRepo.findByProCpnId(proCpnId);
	}
}
