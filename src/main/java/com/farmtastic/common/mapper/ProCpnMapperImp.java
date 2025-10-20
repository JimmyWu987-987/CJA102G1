package com.farmtastic.common.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.farmtastic.common.enums.ApplScope;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.dto.ProCpnResponseDTO;
import com.farmtastic.procpn.model.ProCpnVO;

@Component
public class ProCpnMapperImp implements ProCpnMapper {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

	public ProCpnFormDTO toFormDTO(ProCpnVO vo) {
		if (vo == null) {
			return null;
		}

		ProCpnFormDTO dto = new ProCpnFormDTO();

		// 1️基本欄位直接轉
		dto.setProCpnId(vo.getProCpnId());
		dto.setCpnName(vo.getCpnName());
		dto.setDiscType(vo.getDiscType());
		dto.setDiscValue(vo.getDiscValue());
		dto.setMinSpend(vo.getMinSpend());
		dto.setStartDate(vo.getStartDate());
		dto.setValidDays(vo.getValidDays());
		dto.setCpnDesc(vo.getCpnDesc());

		// 2️枚舉欄位（直接轉即可，因為是 Enum）
		dto.setApplScope(vo.getApplScope());
		dto.setIsActive(vo.getIsActive());

		// ✅ 注意：
		// 這裡不需要轉 expDate（到期日），因為它是衍生欄位，
		// 若你要顯示在編輯表單，可以在 Controller 額外處理。

		return dto;
	}

	@Override
	public ProCpnResponseDTO toResponseDTO(ProCpnVO vo) {
		ProCpnResponseDTO dto = new ProCpnResponseDTO();
		dto.setProCpnId(vo.getProCpnId());
		dto.setCpnName(vo.getCpnName());
		// 格式化折扣
		if (vo.getDiscType() != null && vo.getDiscValue() != null) {
			switch (vo.getDiscType()) {
			case PERCENTAGE -> dto.setDiscountInfo(vo.getDiscValue() + "折");
			case FULL_REDUCTION -> dto.setDiscountInfo("滿" + vo.getMinSpend() + "折" + vo.getDiscValue());
			default -> dto.setDiscountInfo("未設定");
			}
		}

		// validDays → expDate
		if (vo.getValidDays() != null) {
			dto.setExpDate(LocalDate.now().plusDays(vo.getValidDays()));
		} else {
			dto.setExpDate(null);
		}

		dto.setStatus(vo.getIsActive() != null ? vo.getIsActive().name() : "UNKNOWN");
		return dto;
	}

	// 給後端所有
	public ProCpnAdminDTO toAdminDTO(ProCpnVO vo) {
		if (vo == null)
			return null;
		ProCpnAdminDTO dto = new ProCpnAdminDTO();

		dto.setProCpnId(vo.getProCpnId());
		dto.setCpnName(vo.getCpnName());
		dto.setDiscType(vo.getDiscType() != null ? vo.getDiscType().getText() : "未知類型");
		dto.setDiscValue(vo.getDiscValue());
		dto.setMinSpend(vo.getMinSpend());

		// 日期與有效期
		if (vo.getStartDate() != null) {
			dto.setStartDate(vo.getStartDate());
			if (vo.getValidDays() != null)
				dto.setExpDate(vo.getStartDate().plusDays(vo.getValidDays()));
		}

		dto.setValidDays(vo.getValidDays());
		dto.setCpnDesc(vo.getCpnDesc());

		// 狀態
		if (vo.getIsActive() != null) {
			dto.setIsActive(vo.getIsActive().name());
			dto.setStatusText(vo.getIsActive() == IsActive.ACTIVE ? "啟用中" : "未啟用");
		} else {
			dto.setIsActive("UNKNOWN");
			dto.setStatusText("未知");
		}

		// 適用範圍
		dto.setApplScope(vo.getApplScope() != null ? String.valueOf(vo.getApplScope()) : "N/A");
		dto.setApplScopeText(scopeText(vo.getApplScope()));

		// 建立時間格式化
		dto.setCrtAt(vo.getCrtAt() != null ? vo.getCrtAt().toLocalDateTime().format(FORMATTER) : "");

		return dto;
	}

	private String scopeText(ApplScope scope) {
		if (scope == null)
			return "未設定";
		return switch (scope) {
		case ALL -> "全館通用";
		case FARMER -> "指定小農";
		case PRODUCT -> "指定商品";
		default -> "未知";
		};
	}

	// from DTO to VO
	public ProCpnVO toEntity(ProCpnFormDTO dto) {
		if (dto == null) {
			return null;
		}

		ProCpnVO vo = new ProCpnVO();
		vo.setProCpnId(dto.getProCpnId());
		vo.setCpnName(dto.getCpnName());
		vo.setDiscType(dto.getDiscType());
		vo.setDiscValue(dto.getDiscValue());
		vo.setMinSpend(dto.getMinSpend());
		// 防止 NullPointerException
		if (dto.getStartDate() != null) {
			vo.setStartDate((dto.getStartDate()));
		} else {
			// 若表單未填，給預設今天
			vo.setStartDate(LocalDate.now());
		}
		vo.setValidDays(dto.getValidDays());
		vo.setCpnDesc(dto.getCpnDesc());
		vo.setApplScope(dto.getApplScope());
		vo.setIsActive(dto.getIsActive());
		return vo;
	}
}
