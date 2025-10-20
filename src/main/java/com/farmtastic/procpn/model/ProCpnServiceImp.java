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

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.memprocpn.model.MemProCpnRepository;

@Service("proCpnService")
public class ProCpnServiceImp implements ProCpnService {

	private final ProCpnRepository proCpnRepo;
	private final MemProCpnRepository memProCpnRepo;
	private final ProCpnMapper proCpnMapper;

	@Autowired
	public ProCpnServiceImp(ProCpnRepository proCpnRepo, MemProCpnRepository memProCpnRepo, ProCpnMapper proCpnMapper) {
		this.proCpnRepo = proCpnRepo;
		this.memProCpnRepo = memProCpnRepo;
		this.proCpnMapper = proCpnMapper;
	}

//新增
	@Override
	public void addProCpn(ProCpnVO procpnVO) {
		proCpnRepo.save(procpnVO);
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
