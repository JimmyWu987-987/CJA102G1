package com.farmtastic.actcpn.model;

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

@Service("actCpnService")
public class ActCpnServiceImp implements ActCpnService {
	@Autowired
	ActCpnRepository repository;

//新增
	@Override
	public void addActCpn(ActCpnVO actcpnVO) {
		repository.save(actcpnVO);
	}

//更新
	@Override
	public void updateActCpn(ActCpnVO actcpnVO) {
		repository.save(actcpnVO);
	}

// 刪除
	@Override
	public void deleteActCpn(Integer id) {
		repository.deleteById(id);
	}

//查單筆
	// 外部介面層（Controller）用實體或 null
	@Override
	public ActCpnVO getOneActCpn(Integer actCpnId) {
		Optional<ActCpnVO> optional = repository.findById(actCpnId);
		return optional.orElse(null);
	}

	// 內部邏輯層（Service）用 Optional
	public Optional<ActCpnVO> getById(Integer actCpnId) {
		return repository.findById(actCpnId);
	}

//查全部
	@Override
	public List<ActCpnVO> getAll() {
		return repository.findAll();
	}

	// 改變卷狀態 啟用或停用

	public void changeActCpnStatus(Integer actCpnId, IsActive status) {
		ActCpnVO actcpnVO = repository.findById(actCpnId).orElseThrow();
		if (actcpnVO.getIsActive().equals(status))
			return; // 避免重複設定
		actcpnVO.setIsActive(status);
		repository.save(actcpnVO);
	}

	// 查啟用券
	@Override
	public List<ActCpnVO> getActiveActCpn() {
		return repository.findByIsActive(IsActive.ACTIVE);
	}

	// 名稱模糊搜尋
	@Override
	public List<ActCpnVO> searchActCpnByName(String keyword) {
		return repository.findByCpnNameContaining(keyword);
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public List<ActCpnVO> findActCpnByDateRange(Date start, Date end) {
		return repository.findByStartDateBetween(start, end);
	}

	// 提供前台可領取清單
	@Override
	public List<ActCpnVO> findAvailableForMember() {
		return repository.findAvailableForMember();
	}

//分頁
	@Override
	public Page<ActCpnVO> findPagedActCpn(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("actCpnId").ascending());
		return repository.findAll(pageable);
	}

	// 3點自動停過期卷
	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Taipei")
	@Override
	public void deactivateExpiredCoupons() {
		repository.deactivateExpiredCoupons();
		System.out.println("[Scheduler] 自動停用過期折價券完成：" + LocalDate.now());
	}

}
