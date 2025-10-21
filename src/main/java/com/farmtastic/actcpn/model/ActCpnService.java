package com.farmtastic.actcpn.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
	List<ActCpnVO> findByKeyword(String keyword);

	// 停用所有過期折價券（排程）
	void deactivateExpiredCoupons();

	// 查啟用中且在有效日期內的券（前台領券用）
	List<ActCpnVO> findAvailableForMember();

	// 查啟用中折價券
	List<ActCpnVO> getActiveActCpn();

	// 改變卷狀態
	void changeActCpnStatus(Integer ActCpnId, IsActive status);

	List<ActCpnVO> filterByDateRange(LocalDate start, LocalDate end);

}
