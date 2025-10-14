package com.farmtastic.procpn.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;

@Service("proCpnService")
public class ProCpnServiceImp implements ProCpnService {
	@Autowired
	ProCpnRepository repository;
	@Autowired
	ProCpnMapper mapper;

	@Autowired
	public ProCpnServiceImp(ProCpnRepository repo, ProCpnMapper mapper) {
		this.repository = repo;
		this.mapper = mapper;
	}

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

//查全部
	@Override
	public List<ProCpnAdminDTO> findAllProCpn() {
		// (vo) -> mapper.toAdminDTO(vo) .collect把轉換後的資料流收集回一個 List。
		// List<ProCpnAdminDTO>
		return repository.findAll().stream().map(mapper::toAdminDTO).collect(Collectors.toList());
	}

	// 單筆查詢
	public Optional<ProCpnVO> getById(Integer proCpnId) {
		return repository.findById(proCpnId);
	}

	// 查啟用券
	@Override
	public List<ProCpnAdminDTO> getActiveProCpn() {
		return repository.findByIsActive(IsActive.ACTIVE).stream().map(mapper::toAdminDTO).collect(Collectors.toList());
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
	public List<ProCpnAdminDTO> searchProCpnByName(String keyword) {
		List<ProCpnVO> resultList = repository.findByCpnNameContaining(keyword);
		return resultList.stream().map(mapper::toAdminDTO).collect(Collectors.toList());
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public List<ProCpnAdminDTO> findProCpnByDateRange(Date start, Date end) {
		List<ProCpnVO> resultList = repository.findByStartDateBetween(start, end);
		return resultList.stream().map(mapper::toAdminDTO).collect(Collectors.toList());
	}

	@Override
	public void deactivateExpiredCoupons() {
		// TODO Auto-generated method stub

	}

}
