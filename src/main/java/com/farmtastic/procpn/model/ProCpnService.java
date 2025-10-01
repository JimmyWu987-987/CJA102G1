package com.farmtastic.procpn.model;

import java.util.List;

import com.farmtastic.common.enums.IsActive;

//多個Service切換實作不改 Controller
public interface ProCpnService {
	void addProCpn(ProCpnVO vo);

	void updateProCpn(ProCpnVO vo);

	ProCpnVO getOneProCpn(Integer id);

	List<ProCpnVO> getAll();

	List<ProCpnVO> getActiveCoupons();// 查詢卷

	void changeCouponStatus(Integer proCpnId, IsActive status);
	// 改變卷狀態
}
