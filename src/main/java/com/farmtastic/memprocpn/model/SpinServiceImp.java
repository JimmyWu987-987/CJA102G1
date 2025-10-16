package com.farmtastic.memprocpn.model;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service
public class SpinServiceImp {
	@Autowired
	private ProCpnRepository proCpnRepo;
	@Autowired
	private MemProCpnRepository memProCpnRepo;
	@Autowired
	private MemProCpnServiceImp memProCpnService;

	private Random random = new Random();

	public String spinAndGiveCoupon(Integer memId) {
		// 模擬機率
		int roll = random.nextInt(100);
		ProCpnVO coupon = null;
		if (roll < 5) {
			coupon = proCpnRepo.findByCpnName("轉盤折200").orElse(null);
		} else if (roll < 10) {
			coupon = proCpnRepo.findByCpnName("轉盤折100").orElse(null);
		} else {
			return "沒中獎，再接再厲！";
		}
		if (coupon != null) {
			// ✅ 用原本的發券方法
			memProCpnService.giveCoupon(memId, coupon.getProCpnId());
			return "🎉 恭喜獲得：" + coupon.getCpnName();
		}

		return "💨 沒中獎，再接再厲！";
	}
}