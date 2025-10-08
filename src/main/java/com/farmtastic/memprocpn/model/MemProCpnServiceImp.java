package com.farmtastic.memprocpn.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.mapper.MemProCpnMapperImp;

@Service("memProCpnService")
public class MemProCpnServiceImp {
	@Autowired
	MemProCpnRepository repository;
	@Autowired
	private MemProCpnMapperImp mapper;

	// 新增
	public void addMemProCpn(MemProCpnVO memProCpnVO) {
		repository.save(memProCpnVO);
	}

	// 修改
	public void updateMemProCpn(MemProCpnVO memProCpnVO) {
		repository.save(memProCpnVO);
	}

	// 查全部
	public List<MemProCpnVO> getAll() {
		return repository.findAll();
	}

	// 查「某會員」未使用且有效折價券
	public List<MemProCpnVO> getValidCpnsByMember(Integer memId) {
		return repository.findValidCpnByMember(memId);
	}
}
