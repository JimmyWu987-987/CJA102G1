package com.farmtastic.common.mapper;

import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.dto.ProCpnResponseDTO;
import com.farmtastic.procpn.model.ProCpnVO;

public interface ProCpnMapper {
	// 單筆轉換
	ProCpnResponseDTO toResponseDTO(ProCpnVO vo);

	/** 單筆轉換 → 給前台或 API 回傳用 */
	ProCpnFormDTO toFormDTO(ProCpnVO vo);

	/** 後台管理清單用 */
	ProCpnAdminDTO toAdminDTO(ProCpnVO vo);

//	ProCpnDetailDTO toDetailDTO(ProCpnVO vo);
	/** 表單轉回資料庫實體 */
	ProCpnVO toEntity(ProCpnFormDTO form);

//	MemberCouponDTO toMemberCouponDTO(ProCpnVO vo);
}
