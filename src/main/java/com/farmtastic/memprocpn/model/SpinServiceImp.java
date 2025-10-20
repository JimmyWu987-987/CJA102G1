package com.farmtastic.memprocpn.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service
public class SpinServiceImp {
//操作 Redis 的萬能工具箱，能存取字串、物件、集合、雜湊、遞減、過期時間

	private final StringRedisTemplate stringRedisTemplate;
	private final ProCpnRepository proCpnRepo;
	private Random random = new Random();

	@Autowired
	public SpinServiceImp(StringRedisTemplate stringRedisTemplate, ProCpnRepository proCpnRepo) {
		this.stringRedisTemplate = stringRedisTemplate;
		this.proCpnRepo = proCpnRepo;
	}

	// 抽獎後存入REDIS
	// 之後SpinSyncScheduler排程發卷
	public Map<String, Object> spinAndGiveCoupon(Integer memId) {
		Map<String, Object> result = new HashMap<>();
		// 建立今日唯一 Redis key
		// String key = buildRedisKey(memId);

		// 檢查是否已抽過
//		if (hasSpunToday(key)) {
//			return failResult("您今天已抽過，請明天再來！");
		// }
		// 設定抽獎記錄 + 一天後過期
		// markUserAsSpun(key);

		// 抽獎結果（呼叫分離的邏輯）
		Map<String, Object> drawResult = drawCoupon();
		// 中獎 → 記錄在 Redis 暫存區（而非立即進 DB）
		if ("WIN".equals(drawResult.get("status"))) {
			// 新增暫存中獎紀錄 Key-Value spin: memId pending: coupon.getProCpnId()
			// 用QUEUE
			queuePendingReward(memId, (ProCpnVO) drawResult.get("coupon"));
		}
		System.out.println("中獎紀錄" + drawResult);
		return drawResult;
	}

	/**
	 * 建立今日唯一 Redis key
	 */
	private String buildRedisKey(Integer memId) {
		return "spin:user:" + memId + ":" + LocalDate.now();
	}

	/**
	 * 檢查今天是否已抽過
	 */
	private boolean hasSpunToday(String key) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
	}

	/**
	 * 標記今日已抽，設定 1 天過期
	 */
	private void markUserAsSpun(String key) {
		stringRedisTemplate.opsForValue().set(key, "done", 1, TimeUnit.DAYS);
	}

	/**
	 * 抽獎邏輯：決定是否中獎、中什麼獎
	 */
	private Map<String, Object> drawCoupon() {
		Map<String, Object> result = new HashMap<>();
		int roll = random.nextInt(100);
		ProCpnVO coupon = null;

		if (roll < 99) {
			coupon = findCouponOrThrow("轉盤折200");
			result.put("status", "WIN");
			result.put("coupon", coupon);
			result.put("result", coupon.getCpnName());
		} else if (roll < 1) {
			coupon = findCouponOrThrow("轉盤折100");
			result.put("status", "WIN");
			result.put("coupon", coupon);
			result.put("result", coupon.getCpnName());
		} else {
			result.put("status", "LOSE");
			result.put("message", "沒中獎，再接再厲！");
		}

		return result;
	}

	/**
	 * 若中獎，暫存至 Redis Queue，等待排程發券
	 */
	private void queuePendingReward(Integer memId, ProCpnVO coupon) {
		String queueKey = "spin:pending:queue";
		// .opsForList()取得List 操作器
		// .rightPush(...) 向清單右邊（尾端）插入
		stringRedisTemplate.opsForList().rightPush(queueKey, memId + ":" + coupon.getProCpnId());
	}

	/**
	 * 查找折價券，不存在則丟例外
	 */
	private ProCpnVO findCouponOrThrow(String cpnName) {
		return proCpnRepo.findByCpnName(cpnName).orElseThrow(() -> new IllegalStateException("⚠️ 折價券不存在: " + cpnName));
	}

	private ProCpnVO findCouponOrThrow(Integer proCpnId) {
		return proCpnRepo.findById(proCpnId).orElseThrow(() -> new IllegalStateException("⚠️ 折價券不存在: " + proCpnId));
	}

	/**
	 * 封裝錯誤回傳格式
	 */
	private Map<String, Object> failResult(String message) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("message", message);
		return result;
	}

}