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
	@Autowired
	ProCpnRepository proCpnRepository;
	@Autowired
	MemProCpnRepository memProCpnRepository;
	@Autowired
	ProCpnMapper mapper;

//新增
	@Override
	public void addProCpn(ProCpnVO procpnVO) {
		proCpnRepository.save(procpnVO);
	}

//更新
	@Override
	public void updateProCpn(ProCpnVO procpnVO) {
		proCpnRepository.save(procpnVO);
	}

//查全部
	@Override
	public List<ProCpnVO> findAll() {
		return proCpnRepository.findAll();
	}

	// 單筆查詢
	public Optional<ProCpnVO> getById(Integer proCpnId) {
		return proCpnRepository.findById(proCpnId);
	}

	// 查啟用券
	@Override
	public List<ProCpnVO> getActiveProCpn() {
		return proCpnRepository.findByIsActive(IsActive.ACTIVE);
	}

	// 改變卷狀態 啟用或停用
	@Override
	public void changeProCpnStatus(Integer proCpnId, IsActive status) {
		ProCpnVO procpnVO = proCpnRepository.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive().equals(status))
			return; // 避免重複設定
		procpnVO.setIsActive(status);
		proCpnRepository.save(procpnVO);
	}

	// 改變卷狀態 啟用或停用
	@Override
	@Transactional
	public void toggleProCpnStatus(Integer proCpnId) {
		ProCpnVO coupon = proCpnRepository.findById(proCpnId)
				.orElseThrow(() -> new IllegalArgumentException("找不到折價券：" + proCpnId));

		// 切換狀態
		if (coupon.getIsActive() == IsActive.ACTIVE) {
			coupon.setIsActive(IsActive.INACTIVE);
		} else {
			coupon.setIsActive(IsActive.ACTIVE);
		}

		proCpnRepository.save(coupon);
	}

	// 名稱模糊搜尋
	@Override
	public Page<ProCpnVO> findByKeywordPaged(String keyword, Pageable pageable) {
		return proCpnRepository.findByCpnNameContaining(keyword, pageable);
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public Page<ProCpnVO> filterByDateRange(Date start, Date end, Pageable pageable) {
		if (start != null && end != null)
			return proCpnRepository.findByStartDateBetween(start, end, pageable);
		else if (start != null)
			return proCpnRepository.findByStartDateAfter(start, pageable);
		else if (end != null)
			return proCpnRepository.findByStartDateBefore(end, pageable);
		else
			return proCpnRepository.findAll(pageable);
	}

//3點自動停過期卷
	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Taipei")
	@Override
	public void deactivateExpiredCoupons() {
		proCpnRepository.deactivateExpiredCoupons();
		System.out.println("[Scheduler] 自動停用過期折價券完成：" + LocalDate.now());
	}

//提供前台可領取清單
	@Override
	public List<ProCpnVO> findAvailableForMember() {
		return proCpnRepository.findAvailableForMember();
	}

//分頁
	public Page<ProCpnVO> findPagedProCpn(Pageable pageable) {
		return proCpnRepository.findAll(pageable);
	}

	@Override
	public BigDecimal calculateDiscount(ProCpnVO coupon, BigDecimal originalPrice) {
		return null;
	}

}
