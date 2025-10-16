package com.farmtastic.common.utility;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farmtastic.memactcpn.model.MemActCpnRepository;
import com.farmtastic.memprocpn.model.MemProCpnRepository;
import com.farmtastic.memprocpn.model.MemProCpnVO;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.reg.model.RegVO;

@Component
public class CouponBinder {
	@Autowired
	private MemProCpnRepository memProCpnRepo;
	@Autowired
	private MemActCpnRepository memActCpnRepo;

	public void bindProCoupon(ProOrderVO orderVO) {
		if (orderVO == null || orderVO.getMemProCpnVO() == null) {
			orderVO.setMemProCpnVO(null);
			return;
		}
		Integer cpnHolderDetailId = orderVO.getMemProCpnVO().getCpnHolderDetailId();
		// 檢查是否有選擇有效優惠券
		if (cpnHolderDetailId == null || cpnHolderDetailId == 0) {
			orderVO.setMemProCpnVO(null);
			return;
		}
		// 從資料庫重新載入該券（變成 managed 狀態）
		Optional<MemProCpnVO> optional = memProCpnRepo.findById(cpnHolderDetailId);
		if (optional.isPresent()) {
			orderVO.setMemProCpnVO(optional.get());
		} else {
			orderVO.setMemProCpnVO(null);
		}
	}

	public void bindActCoupon(RegVO regVO) {
		if (regVO == null)
			return;

		Integer id = regVO.getCpnHolderDetailId();
		if (id == null || id == 0 || !memActCpnRepo.existsById(id)) {
			regVO.setCpnHolderDetailId(null);
		}
	}
//	public void bindActCoupon(RegVO regVO) {
//		if (regVO == null || regVO.getCpnHolderDetailId() == null) {
//			regVO.setCpnHolderDetailId(null);
//			return;
//		}
//		Integer cpnHolderDetailId = regVO.getCpnHolderDetailId();
//		// 檢查是否有選擇有效優惠券
//		if (cpnHolderDetailId == null || cpnHolderDetailId == 0) {
//			regVO.setCpnHolderDetailId(null);
//			return;
//		}
//		Optional<MemActCpnVO> optional = memActCpnRepo.findById(cpnHolderDetailId);
//		if (optional.isPresent()) {
//			regVO.setCpnHolderDetailId(cpnHolderDetailId);
//		} else {
//			regVO.setCpnHolderDetailId(null);
//		}
//	}
}
