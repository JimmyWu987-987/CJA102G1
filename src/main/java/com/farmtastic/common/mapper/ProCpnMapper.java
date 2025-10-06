package com.farmtastic.common.mapper;

import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.dto.ProCpnResponseDTO;
import com.farmtastic.procpn.model.ProCpnVO;

public interface ProCpnMapper {
	// 單筆轉換
	ProCpnResponseDTO toResponseDTO(ProCpnVO vo);

	ProCpnAdminDTO toAdminDTO(ProCpnVO vo);
//	ProCpnDetailDTO toDetailDTO(ProCpnVO vo);

//	MemberCouponDTO toMemberCouponDTO(ProCpnVO vo);
}
