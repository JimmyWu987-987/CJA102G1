package com.farmtastic.actcpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ActCpnService")
public class ActCpnServiceImp implements ActCpnService {
	@Autowired
	ActCpnRepository repository;

	@Override
	public void addActCpn(ActCpnVO actcpnVO) {
		repository.save(actcpnVO);
	}

	@Override
	public void updateActCpn(ActCpnVO actcpnVO) {
		repository.save(actcpnVO);
	}

	@Override
	public ActCpnVO getOneActCpn(Integer actCpnId) {
		Optional<ActCpnVO> optional = repository.findById(actCpnId);
		return optional.orElse(null);
	}

	@Override
	public List<ActCpnVO> getAll() {
		return repository.findAll();
	}

	@Override
	public List<ActCpnVO> getActiveCoupons() {

		return repository.findByIsActive((byte) 1);
	}

	@Override
	public void activateCoupon(Integer actCpnId) {
		ActCpnVO actcpnVO = repository.findById(actCpnId).orElseThrow();
		if (actcpnVO.getIsActive() == 1) {
			return;
		}
		actcpnVO.setIsActive((byte) 1);
		repository.save(actcpnVO);
	}

	@Override
	public void deactivateCoupon(Integer actCpnId) {
		ActCpnVO actcpnVO = repository.findById(actCpnId).orElseThrow();
		if (actcpnVO.getIsActive() == 0) {
			return; // 已經停用，不再動作
		}
		actcpnVO.setIsActive((byte) 0);
		repository.save(actcpnVO);
	}

}
