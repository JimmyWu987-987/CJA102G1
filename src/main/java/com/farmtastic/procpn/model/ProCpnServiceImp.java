package com.farmtastic.procpn.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ProCpnMapper;

@Service("proCpnService")
public class ProCpnServiceImp implements ProCpnService {
	@Autowired
	ProCpnRepository repository;
	@Autowired
	ProCpnMapper mapper;

//新增
	@Override
	public void addProCpn(ProCpnVO procpnVO) {
		repository.save(procpnVO);
	}

//更新
	@Override
	public void updateProCpn(ProCpnVO procpnVO) {
		repository.save(procpnVO);
	}

// 刪除
	@Override
	public void deleteProCpn(Integer id) {
		repository.deleteById(id);
	}

//查全部
	@Override
	public List<ProCpnVO> findAll() {
		return repository.findAll();
	}

	// 單筆查詢
	public Optional<ProCpnVO> getById(Integer proCpnId) {
		return repository.findById(proCpnId);
	}

	// 查啟用券
	@Override
	public List<ProCpnVO> getActiveProCpn() {
		return repository.findByIsActive(IsActive.ACTIVE);
	}

	// 改變卷狀態 啟用或停用
	@Override
	public void changeProCpnStatus(Integer proCpnId, IsActive status) {
		ProCpnVO procpnVO = repository.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive().equals(status))
			return; // 避免重複設定
		procpnVO.setIsActive(status);
		repository.save(procpnVO);
	}

	// 名稱模糊搜尋
	@Override
	public List<ProCpnVO> searchProCpnByName(String keyword) {
		return repository.findByCpnNameContaining(keyword);
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public List<ProCpnVO> findProCpnByDateRange(Date start, Date end) {
		return repository.findByStartDateBetween(start, end);
	}

//3點自動停過期卷
	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Taipei")
	@Override
	public void deactivateExpiredCoupons() {
		repository.deactivateExpiredCoupons();
		System.out.println("[Scheduler] 自動停用過期折價券完成：" + LocalDate.now());
	}

//提供前台可領取清單
	@Override
	public List<ProCpnVO> findAvailableForMember() {
		return repository.findAvailableForMember();
	}

//分頁
	public Page<ProCpnVO> findPagedProCpn(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("proCpnId").ascending());
		return repository.findAll(pageable);
	}

	@Override
	public BigDecimal calculateDiscount(ProCpnVO coupon, BigDecimal originalPrice) {
		// TODO Auto-generated method stub
		return null;
	}

}
