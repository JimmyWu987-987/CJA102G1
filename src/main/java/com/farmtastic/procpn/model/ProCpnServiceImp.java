package com.farmtastic.procpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;

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

	// 啟用券
	@Override
	public void activateCoupon(Integer proCpnId) {
		ProCpnVO procpnVO = repository.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive() == IsActive.ACTIVE) {
			return;// 已經啟用，不再動作
		}
		procpnVO.setIsActive((IsActive.ACTIVE));
		repository.save(procpnVO);
	}

	// 停用卷
	@Override
	public void deactivateCoupon(Integer proCpnId) {
		ProCpnVO procpnVO = repository.findById(proCpnId).orElseThrow();
		if (procpnVO.getIsActive() == IsActive.INACTIVE) {
			return; // 已經停用，不再動作
		}
		procpnVO.setIsActive(IsActive.INACTIVE);
		repository.save(procpnVO);
	}
}
