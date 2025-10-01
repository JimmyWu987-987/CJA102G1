package com.farmtastic.memprocpn.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memProCpnService")
public class MemProCpnServiceImp {
	@Autowired
	MemProCpnRepository repository;

	public void addMemProCpn(MemProCpnVO memProCpnVO) {
		repository.save(memProCpnVO);
	}

	public void updateMemProCpn(MemProCpnVO memProCpnVO) {
		repository.save(memProCpnVO);
	}

	public List<MemProCpnVO> getAll() {
		return repository.findAll();
	}
}
