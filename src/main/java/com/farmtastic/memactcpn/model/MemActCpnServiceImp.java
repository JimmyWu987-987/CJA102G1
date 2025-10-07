package com.farmtastic.memactcpn.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.CpnUseStatus;

@Service("MemActCpnService")
public class MemActCpnServiceImp {
	@Autowired
	MemActCpnRepository repository;

	public void addMemActCpn(MemActCpnVO memActCpnVO) {
		repository.save(memActCpnVO);
	}

	public void updateMemProCpn(MemActCpnVO memActCpnVO) {
		repository.save(memActCpnVO);
	}

	public List<MemActCpnVO> getAll() {
		return repository.findAll();
	}

	// 查某張券的「已使用」清單
	public List<MemActCpnVO> getUsedRecords(Integer cpnId) {
		return repository.findUsedRecords(cpnId, CpnUseStatus.USED);
	}

}
