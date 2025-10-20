package com.farmtastic.memprocpn.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.CpnSource;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service
public class SpinServiceImp {
//操作 Redis 的萬能工具箱，能存取字串、物件、集合、雜湊、遞減、過期時間

	private final StringRedisTemplate stringRedisTemplate;
	private final ProCpnRepository proCpnRepo;
	private List<ProCpnVO> lotteryCoupons = new ArrayList<>();
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
	 * 更新抽獎池 — 從資料庫撈出所有 cpn_source = LOTTERY 的折價券
	 */
	public void refreshLotteryPool() {
		lotteryCoupons = proCpnRepo.findAllByCpnSource(CpnSource.LOTTERY);
		System.out.println(" 已更新抽獎池，共 " + lotteryCoupons.size() + " 張券");
	}

	public List<ProCpnVO> getLotteryCoupons() {
		if (lotteryCoupons == null || lotteryCoupons.isEmpty()) {
			refreshLotteryPool();
		}
		return lotteryCoupons;
	}

	/**
	 * 抽獎邏輯：決定是否中獎、中什麼獎
	 */
	private Map<String, Object> drawCoupon() {
		Map<String, Object> result = new HashMap<>();
		// 如果 pool 未初始化，立即刷新
		if (lotteryCoupons == null || lotteryCoupons.isEmpty()) {
			refreshLotteryPool();
		}
		if (lotteryCoupons == null || lotteryCoupons.isEmpty()) {
			result.put("status", "ERROR");
			result.put("message", "目前沒有可抽的折價券！");
			return result;
		}
		int roll = random.nextInt(100); // 0~99
		ProCpnVO selected = null;

		System.out.println("抽獎 roll = " + roll);
		for (ProCpnVO coupon : lotteryCoupons) {
			BigDecimal value = coupon.getDiscValue();

			if (value.intValue() <= 100 && roll < 99) {
				selected = coupon;
				break;
			} else if (value.intValue() <= 200 && roll < 20) {
				selected = coupon;
				break;
			} else if (value.intValue() <= 300 && roll < 5) {
				selected = coupon;
				break;
			}
		}
		if (selected != null) {
			result.put("status", "WIN");
			result.put("coupon", selected);
			result.put("result", "🎉 恭喜中獎！獲得「" + selected.getCpnName() + "」");
		} else {
			result.put("status", "LOSE");
			result.put("message", "💨 沒中獎，再接再厲！");
		}
		System.out.println("順序 = " + result);
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