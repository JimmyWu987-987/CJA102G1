package com.farmtastic.common.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.dto.MemProCpnFormDTO;
import com.farmtastic.memprocpn.model.MemProCpnVO;
import com.farmtastic.procpn.model.ProCpnVO;

@Component
public class MemProCpnMapperImp {
	// VO → DTO (前台顯示用)
	public MemProCpnFormDTO toDTO(MemProCpnVO vo) {
		MemProCpnFormDTO dto = new MemProCpnFormDTO();
		dto.setCpnHolderDetailId(vo.getCpnHolderDetailId());
		dto.setCpnUseStatus(vo.getCpnUseStatus());
		dto.setCrtAt(vo.getCrtAt());
		dto.setRcvAt(vo.getRcvAt());
		dto.setEffStart(vo.getEffStart());
		dto.setEffEnd(vo.getEffEnd());
		dto.setUsedAt(vo.getUsedAt());

		if (vo.getProCpnVO() != null) {
			dto.setProCpnId(vo.getProCpnVO().getProCpnId());
			dto.setCpnName(vo.getProCpnVO().getCpnName());
			dto.setDiscType(vo.getProCpnVO().getDiscType());
			dto.setDiscValue(vo.getProCpnVO().getDiscValue());
			dto.setMinSpend(vo.getProCpnVO().getMinSpend());
			dto.setCpnDesc(vo.getProCpnVO().getCpnDesc());
		}
		if (vo.getMemVO() != null) {
			dto.setMemId(vo.getMemVO().getMemId());
		}

		if (vo.getProOrdVO() != null) {
			dto.setProOrdId(vo.getProOrdVO().getProOrdId());
		}
		return dto;
	}

	public MemProCpnVO toEntity(MemProCpnFormDTO dto) {
		MemProCpnVO vo = new MemProCpnVO();

		// 關聯物件只要 new 出 ID 即可（避免查整張表）
		Mem mem = new Mem();
		mem.setMemId(dto.getMemId());
		vo.setMemVO(mem);

		ProCpnVO proCpn = new ProCpnVO();
		proCpn.setProCpnId(dto.getProCpnId());
		vo.setProCpnVO(proCpn);

		vo.setCpnUseStatus((byte) 0);// 要改成Enum
		vo.setCrtAt(LocalDateTime.now());
		vo.setRcvAt(LocalDateTime.now());
		vo.setEffStart(dto.getEffStart());
		vo.setEffEnd(dto.getEffEnd());

		// 尚未使用的券不會有使用時間或訂單
		vo.setUsedAt(null);
		vo.setProOrdVO(null);
		return vo;
	}
}
