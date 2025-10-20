package com.farmtastic.memprocpn.model;

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

	// 從 List 左邊取出一筆（先進先出）
	// 實際發券
	// 成功後刪除（pop 自動移除）
	@Scheduled(fixedRate = 5000) // 每5秒
	public void syncPendingCoupons() {
		String queueKey = "spin:pending:queue";

		while (true) {
			String data = stringRedisTemplate.opsForList().leftPop(queueKey);
			if (data == null)
				break; // 沒資料就結束

			// 解析從 Redis 佇列取出的中獎資料
			// 格式為 "memId:proCpnId"，例如 "123:45"
			// 拆解後取得會員編號與折價券編號
			String[] parts = data.split(":");
			Integer memId = Integer.parseInt(parts[0]);
			Integer proCpnId = Integer.parseInt(parts[1]);

			// 寫入 MySQL
			memProCpnService.giveCoupon(memId, proCpnId);
			// 成功後刪除（pop 自動移除）
			System.out.println(" 已發券: memId=" + memId + ", cpnId=" + proCpnId);
		}
	}
}