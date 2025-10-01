package com.farmtastic.procpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;

//未完成
@Service("proCpnService")
public class ProCpnServiceImp implements ProCpnService {
	@Autowired
	ProCpnRepository repository;

	@Override
	public void addProCpn(ProCpnVO procpnVO) {
		repository.save(procpnVO);
	}

	@Override
	public void updateProCpn(ProCpnVO procpnVO) {
		repository.save(procpnVO);
	}

	@Override
	public ProCpnVO getOneProCpn(Integer proCpnId) {
		Optional<ProCpnVO> optional = repository.findById(proCpnId);
		return optional.orElse(null);
	}

	@Override
	public List<ProCpnVO> getAll() {
		return repository.findAll();
	}

	// 查啟用券
	@Override
	public List<ProCpnVO> getActiveCoupons() {
		return repository.findByIsActive(IsActive.ACTIVE);
	}

	// 改變卷狀態 啟用或停用
	@Override
	public void changeCouponStatus(Integer proCpnId, IsActive status) {
		ProCpnVO procpnVO = repository.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive() == status)
			return; // 避免重複設定
		procpnVO.setIsActive(status);
		repository.save(procpnVO);
	}

}
