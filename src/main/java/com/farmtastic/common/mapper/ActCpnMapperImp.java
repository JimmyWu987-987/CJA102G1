package com.farmtastic.common.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.farmtastic.actcpn.dto.ActCpnFormDTO;
import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.model.ProCpnVO;

@Component
public class ActCpnMapperImp {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

	public ActCpnFormDTO toDTO(ActCpnVO vo) {
		if (vo == null) {
			return null;
		}

		ActCpnFormDTO dto = new ActCpnFormDTO();

		// 1️. 基本欄位直接轉
		dto.setActCpnId(vo.getActCpnId());
		dto.setCpnSource(vo.getCpnSource());
		dto.setCpnName(vo.getCpnName());
		dto.setDiscType(vo.getDiscType());
		dto.setDiscValue(vo.getDiscValue());
		dto.setMinSpend(vo.getMinSpend());
		dto.setStartDate(vo.getStartDate());
		dto.setValidDays(vo.getValidDays());
		dto.setCpnDesc(vo.getCpnDesc());

		// 2️. 枚舉欄位（直接轉即可，因為是 Enum）
		dto.setIsActive(vo.getIsActive());

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

		// 建立時間格式化
		dto.setCrtAt(vo.getCrtAt() != null ? vo.getCrtAt().toLocalDateTime().format(FORMATTER) : "");

		return dto;
	}

	// from DTO to VO
	public ActCpnVO toEntity(ActCpnFormDTO dto) {
		if (dto == null) {
			return null;
		}

		ActCpnVO vo = new ActCpnVO();
		vo.setActCpnId(dto.getActCpnId());
		vo.setCpnSource(dto.getCpnSource());
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
		vo.setIsActive(dto.getIsActive());
		return vo;
	}

}