package com.farmtastic.memprocpn.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service
public class SpinServiceImp {
//操作 Redis 的萬能工具箱，能存取字串、物件、集合、雜湊、遞減、過期時間
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private ProCpnRepository proCpnRepo;

	private Random random = new Random();

	private ProCpnVO findCouponOrThrow(String cpnName) {
		return proCpnRepo.findByCpnName(cpnName).orElseThrow(() -> new IllegalStateException("⚠️ 折價券不存在: " + cpnName));
	}

	public Map<String, Object> spinAndGiveCoupon(Integer memId) {
		Map<String, Object> result = new HashMap<>();
		String key = "spin:user:" + memId;
		// 檢查是否抽過
//		if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
//			return "⚠️ 您今天已抽過，請明天再來！";
//		}
//		// 設定抽獎記錄 + 一天後過期
//		stringRedisTemplate.opsForValue().set(key, "done", 1, TimeUnit.DAYS);
		// 模擬機率
		int roll = random.nextInt(100);
		ProCpnVO coupon = null;
		if (roll < 10) {
			coupon = findCouponOrThrow("轉盤折200");
			result.put("status", "WIN");
			result.put("result", coupon.getCpnName());
		} else if (roll < 25) {
			coupon = findCouponOrThrow("轉盤折100");
			result.put("status", "WIN");
			result.put("result", coupon.getCpnName());
		} else {
			result.put("status", "LOSE");
			result.put("message", "沒中獎，再接再厲！");
		}
		// 中獎 → 記錄在 Redis 暫存區（而非立即進 DB）
		if (coupon != null) {
			// 新增暫存中獎紀錄 Key-Value spin: memId pending: coupon.getProCpnId()
			String prizeKey = "spin:pending:" + memId + ":" + coupon.getProCpnId();
			stringRedisTemplate.opsForValue().set(prizeKey, coupon.getCpnName());
		}
		return result;
	}
}