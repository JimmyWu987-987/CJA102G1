package com.farmtastic.procpn.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.farmtastic.common.enums.IsActive;

//多個Service切換實作不改 Controller
public interface ProCpnService {
	// 資料層操作
	// 暫時先用vo
	void addProCpn(ProCpnVO vo);

	void updateProCpn(ProCpnVO vo);

	List<ProCpnVO> getAll();

	// 給前端展示用（轉成 DTO） // === 業務邏輯 / 前端顯示 ===
	List<ProCpnVO> getActiveProCpn();// 查啟用中折價券

//	List<ProCpnVO> findAll(); // 查全部折價券

	void changeProCpnStatus(Integer proCpnId, IsActive status);// 改變卷狀態

	// Optional<ProCpnAdminDTO> getById(Integer id); // 查單一折價券 (DTO)
	Optional<ProCpnVO> getById(Integer proCpnId);

	// 名稱模糊搜尋
	List<ProCpnVO> findByKeyword(String keyword);

	// 查詢指定日期範圍內的折價券
	Page<ProCpnVO> filterByDateRange(java.util.Date start, java.util.Date end, Pageable pageable);

	// 停用所有過期折價券（排程）
	void deactivateExpiredCoupons();

	// 查啟用中且在有效日期內的券（前台領券用）
	List<ProCpnVO> findAvailableForMember();

	// 分頁
	Page<ProCpnVO> findPagedProCpn(Pageable pageable);

	// 計算折扣金額（for 測試 / 套用邏輯）
	BigDecimal calculateDiscount(ProCpnVO coupon, BigDecimal originalPrice);

	void toggleProCpnStatus(Integer proCpnId);

}
