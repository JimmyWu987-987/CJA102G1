package com.farmtastic.actcpn.model;

import java.util.List;

public interface ActCpnService {
	void addActCpn(ActCpnVO vo);

	void updateActCpn(ActCpnVO vo);

	ActCpnVO getOneActCpn(Integer id);

	List<ActCpnVO> getAll();

	List<ActCpnVO> getActiveCoupons();// 查詢卷

	void activateCoupon(Integer id);// 啟用卷

	void deactivateCoupon(Integer id);// 停用卷
}
