package com.farmtastic.procpn.model;

import java.util.List;

//多個Service切換實作不改 Controller
public interface ProCpnService {
	void addProCpn(ProCpnVO vo);

	void updateProCpn(ProCpnVO vo);

	ProCpnVO getOneProCpn(Integer id);

	List<ProCpnVO> getAll();

	List<ProCpnVO> getActiveCoupons();// 查詢卷

	void activateCoupon(Integer id);// 啟用卷

	void deactivateCoupon(Integer id);// 停用卷
}
