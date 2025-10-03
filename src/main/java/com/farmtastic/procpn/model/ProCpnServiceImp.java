package com.farmtastic.procpn.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.procpn.dto.ProCpnResponseDTO;

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
	public List<ProCpnResponseDTO> findAllProCpn() {
		return repository.findAll().stream().map(vo -> {
			ProCpnResponseDTO dto = new ProCpnResponseDTO();
			// 帶上券 ID（前端操作需要）
			dto.setProCpnId(vo.getProCpnId());
			dto.setCpnName(vo.getCpnName());

			// 格式化折扣資訊
			if (vo.getDiscType() != null && vo.getDiscValue() != null) {
				switch (vo.getDiscType()) {
				case PERCENTAGE -> dto.setDiscountInfo(vo.getDiscValue() + "折");
				case FULL_REDUCTION -> dto.setDiscountInfo("滿" + vo.getMinSpend() + "折" + vo.getDiscValue());
				default -> dto.setDiscountInfo("未設定");
				}
			}

			// 防呆處理 validDays
			if (vo.getValidDays() != null) {
				dto.setExpDate(LocalDate.now().plusDays(vo.getValidDays()));
			} else {
				dto.setExpDate(null); // 或 LocalDate.now() 給預設值
			}

			// 狀態轉換
			dto.setStatus(vo.getIsActive() != null ? vo.getIsActive().name() : "UNKNOWN");

			return dto;
		}).toList();

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
