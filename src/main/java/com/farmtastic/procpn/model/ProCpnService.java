package com.farmtastic.procpn.model;

import java.util.List;
import java.util.Optional;

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;

//多個Service切換實作不改 Controller
public interface ProCpnService {
	// 資料層操作
	// 暫時先用vo
	void addProCpn(ProCpnVO vo);

	void updateProCpn(ProCpnVO vo);

//	List<ProCpnVO> getAll();
	// 給前端展示用（轉成 DTO） // === 業務邏輯 / 前端顯示 ===
	List<ProCpnAdminDTO> getActiveProCpn();// 查啟用中折價券

	List<ProCpnAdminDTO> findAllProCpn(); // 查全部折價券

	void changeProCpnStatus(Integer proCpnId, IsActive status);// 改變卷狀態

	// Optional<ProCpnAdminDTO> getById(Integer id); // 查單一折價券 (DTO)
	public Optional<ProCpnVO> getById(Integer proCpnId);

	List<ProCpnAdminDTO> searchProCpnByName(String keyword);// 名稱模糊搜尋

	// 查詢指定日期範圍內的折價券
	List<ProCpnAdminDTO> findProCpnByDateRange(java.util.Date start, java.util.Date end);

	// 停用所有過期折價券（排程）
	void deactivateExpiredCoupons();

}
