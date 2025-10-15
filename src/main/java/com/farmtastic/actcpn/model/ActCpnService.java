package com.farmtastic.actcpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.farmtastic.common.enums.IsActive;

public interface ActCpnService {
	void addActCpn(ActCpnVO vo);

	void updateActCpn(ActCpnVO vo);

	// 刪除折價券
	void deleteActCpn(Integer id);

	ActCpnVO getOneActCpn(Integer id);

	// 查全部折價券
	List<ActCpnVO> getAll();

	// 查單一折價券
	Optional<ActCpnVO> getById(Integer actCpnId);

	// 名稱模糊搜尋
	List<ActCpnVO> searchActCpnByName(String keyword);

	// 查詢指定日期範圍內的折價券
	List<ActCpnVO> findActCpnByDateRange(java.util.Date start, java.util.Date end);

	// 停用所有過期折價券（排程）
	void deactivateExpiredCoupons();

	// 查啟用中且在有效日期內的券（前台領券用）
	List<ActCpnVO> findAvailableForMember();

	// 分頁
	Page<ActCpnVO> findPagedActCpn(int page, int size);

	// 查啟用中折價券
	List<ActCpnVO> getActiveActCpn();

	// 改變卷狀態
	void changeActCpnStatus(Integer ActCpnId, IsActive status);

}
