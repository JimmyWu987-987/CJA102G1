package com.farmtastic.memprocpn.model;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SpinSyncScheduler {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private MemProCpnServiceImp memProCpnService;

	@Scheduled(fixedRate = 5000) // 每5秒
	public void syncPendingCoupons() {
		Set<String> keys = stringRedisTemplate.keys("spin:pending:*");

		if (keys == null || keys.isEmpty()) {
			return;
		}

		for (String key : keys) {
			try {
				String couponName = stringRedisTemplate.opsForValue().get(key);
				if (couponName == null)
					continue;

				// 從 key 拆出 memId, proCpnId
				String[] parts = key.split(":");
				Integer memId = Integer.parseInt(parts[2]);
				Integer proCpnId = Integer.parseInt(parts[3]);

				// 寫入 MySQL
				memProCpnService.giveCoupon(memId, proCpnId);

				// 刪除 Redis 暫存
				stringRedisTemplate.delete(key);

				System.out.println("✅ 已同步中獎券：" + couponName + " → memberId=" + memId);
			} catch (Exception e) {
				System.err.println("⚠️ 同步失敗：" + key + " → " + e.getMessage());
			}
		}
	}
}
