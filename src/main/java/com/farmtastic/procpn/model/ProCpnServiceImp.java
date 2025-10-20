package com.farmtastic.procpn.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.memprocpn.model.MemProCpnRepository;
import com.farmtastic.memprocpn.model.SpinServiceImp;

@Service("proCpnService")
public class ProCpnServiceImp implements ProCpnService {

	private final ProCpnRepository proCpnRepo;
	private final MemProCpnRepository memProCpnRepo;
	private final ProCpnMapper proCpnMapper;
	private final SpinServiceImp spinServiceImp;

	@Autowired
	public ProCpnServiceImp(ProCpnRepository proCpnRepo, MemProCpnRepository memProCpnRepo, ProCpnMapper proCpnMapper,
			SpinServiceImp spinServiceImp) {
		this.proCpnRepo = proCpnRepo;
		this.memProCpnRepo = memProCpnRepo;
		this.proCpnMapper = proCpnMapper;
		this.spinServiceImp = spinServiceImp;
	}

//新增 
	@Override
	public void addProCpn(ProCpnVO proCpnVO) {
		// 1. 自動命名邏輯
		if (proCpnVO.getCpnName() == null || proCpnVO.getCpnName().isBlank()) {
			String cpnName;

			if (proCpnVO.getDiscType() == DiscountType.PERCENTAGE) {
				// 百分比折扣 → 顯示成「85折」
				int discount = proCpnVO.getDiscValue().intValue();
				cpnName = proCpnVO.getCpnSource().getText() + discount + "折";
			} else {
				// 滿額折抵 → 顯示成「折100元」
				cpnName = proCpnVO.getCpnSource().getText() + "折" + proCpnVO.getDiscValue().intValue();
			}

			proCpnVO.setCpnName(cpnName);
		}
		// 2.儲存折價券
		ProCpnVO saved = proCpnRepo.save(proCpnVO);
		// 3.根據折價券用途決定行為
		switch (proCpnVO.getCpnSource()) {
		case LOTTERY -> {
			// 抽獎券 → 更新抽獎池
			spinServiceImp.refreshLotteryPool();
			System.out.println("新增抽獎券 → 已同步至抽獎池：" + saved.getCpnName());
		}

		case REGISTRATION -> {
			// 註冊券 → 通知會員服務（未來自動發送）
			System.out.println("新增註冊券 → 系統將於會員註冊時自動發送");
			// 可選：註冊時會自動發放，不需立即處理
		}

		case BIRTHDAY -> {
			// 生日券 → 交由排程發放
			System.out.println("新增生日券 → 由排程自動發放");
			// 可選：不需立即動作
		}

		case EVENT -> {
			// 活動券 → 由活動模組掛載
			System.out.println("新增活動券 → 活動模組可引用此券ID：" + saved.getProCpnId());
		}

		default -> {
			// 一般券 → 不需觸發任何自動邏輯
			System.out.println("新增一般券 → " + saved.getCpnName());
		}
		}
	}

//更新
	@Override
	public void updateProCpn(ProCpnVO procpnVO) {
		proCpnRepo.save(procpnVO);
	}

//查全部
	@Override
	public List<ProCpnVO> getAll() {
		return proCpnRepo.findAll();
	}

	// 單筆查詢
	public Optional<ProCpnVO> getById(Integer proCpnId) {
		return proCpnRepo.findById(proCpnId);
	}

	// 查啟用券
	@Override
	public List<ProCpnVO> getActiveProCpn() {
		return proCpnRepo.findByIsActive(IsActive.ACTIVE);
	}

	// 改變卷狀態 啟用或停用
	@Override
	public void changeProCpnStatus(Integer proCpnId, IsActive status) {
		ProCpnVO procpnVO = proCpnRepo.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive().equals(status))
			return; // 避免重複設定
		procpnVO.setIsActive(status);
		proCpnRepo.save(procpnVO);
	}

	// 改變卷狀態 啟用或停用
	@Override
	@Transactional
	public void toggleProCpnStatus(Integer proCpnId) {
		ProCpnVO coupon = proCpnRepo.findById(proCpnId)
				.orElseThrow(() -> new IllegalArgumentException("找不到折價券：" + proCpnId));

		// 切換狀態
		if (coupon.getIsActive() == IsActive.ACTIVE) {
			coupon.setIsActive(IsActive.INACTIVE);
		} else {
			coupon.setIsActive(IsActive.ACTIVE);
		}

		proCpnRepo.save(coupon);
	}

	// 名稱模糊搜尋
	@Override
	public List<ProCpnVO> findByKeyword(String keyword) {
		return proCpnRepo.findByCpnNameContaining(keyword);
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public Page<ProCpnVO> filterByDateRange(Date start, Date end, Pageable pageable) {
		if (start != null && end != null)
			return proCpnRepo.findByStartDateBetween(start, end, pageable);
		else if (start != null)
			return proCpnRepo.findByStartDateAfter(start, pageable);
		else if (end != null)
			return proCpnRepo.findByStartDateBefore(end, pageable);
		else
			return proCpnRepo.findAll(pageable);
	}

//3點自動停過期卷
	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Taipei")
	@Override
	public void deactivateExpiredCoupons() {
		proCpnRepo.deactivateExpiredCoupons();
		System.out.println("[Scheduler] 自動停用過期折價券完成：" + LocalDate.now());
	}

//提供前台可領取清單
	@Override
	public List<ProCpnVO> findAvailableForMember() {
		return proCpnRepo.findAvailableForMember();
	}

//分頁
	public Page<ProCpnVO> findPagedProCpn(Pageable pageable) {
		return proCpnRepo.findAll(pageable);
	}

	@Override
	public BigDecimal calculateDiscount(ProCpnVO coupon, BigDecimal originalPrice) {
		return null;
	}

}
