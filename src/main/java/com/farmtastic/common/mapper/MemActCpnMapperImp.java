package com.farmtastic.common.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.memactcpn.dto.MemActCpnFormDTO;
import com.farmtastic.memactcpn.model.MemActCpnVO;
import com.farmtastic.member.model.Mem;

@Component
public class MemActCpnMapperImp {
	// VO → DTO (前台顯示用)
	public MemActCpnFormDTO toDTO(MemActCpnVO vo) {
		MemActCpnFormDTO dto = new MemActCpnFormDTO();
		dto.setCpnHolderDetailId(vo.getCpnHolderDetailId());
		dto.setCpnUseStatus(vo.getCpnUseStatus());
		dto.setCrtAt(vo.getCrtAt());
		dto.setRcvAt(vo.getRcvAt());
		dto.setEffStart(vo.getEffStart());
		dto.setEffEnd(vo.getEffEnd());
		dto.setUsedAt(vo.getUsedAt());

		if (vo.getActCpnVO() != null) {
			dto.setActCpnId(vo.getActCpnVO().getActCpnId());
			dto.setCpnName(vo.getActCpnVO().getCpnName());
			dto.setDiscType(vo.getActCpnVO().getDiscType());
			dto.setDiscValue(vo.getActCpnVO().getDiscValue());
			dto.setMinSpend(vo.getActCpnVO().getMinSpend());
			dto.setCpnDesc(vo.getActCpnVO().getCpnDesc());
		}
		if (vo.getMemVO() != null) {
			dto.setMemId(vo.getMemVO().getMemId());
		}

		return dto;
	}

	public MemActCpnVO toEntity(MemActCpnFormDTO dto) {
		MemActCpnVO vo = new MemActCpnVO();

		// 關聯物件只要 new 出 ID 即可（避免查整張表）
		Mem mem = new Mem();
		mem.setMemId(dto.getMemId());
		vo.setMemVO(mem);

		ActCpnVO actCpn = new ActCpnVO();
		actCpn.setActCpnId(dto.getActCpnId());
		vo.setActCpnVO(actCpn);

		vo.setCpnUseStatus(CpnUseStatus.UNUSED);// 要改成Enum
		vo.setCrtAt(LocalDateTime.now());
		vo.setRcvAt(LocalDateTime.now());
		vo.setEffStart(dto.getEffStart());
		vo.setEffEnd(dto.getEffEnd());

		// 尚未使用的券不會有使用時間或訂單
		vo.setUsedAt(null);
		// vo.setProOrdVO(null);
		return vo;
	}
}