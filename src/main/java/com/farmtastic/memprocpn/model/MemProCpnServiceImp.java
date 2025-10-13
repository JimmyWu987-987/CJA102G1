package com.farmtastic.memprocpn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.MemProCpnMapperImp;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service("memProCpnService")
public class MemProCpnServiceImp {

	@Autowired
	private ProCpnRepository proCpnRepository; // ✅ 查找折價券名稱用這個
	@Autowired
	MemProCpnRepository memProCpnRepository;
	@Autowired
	private MemProCpnMapperImp mapper;
	@Autowired
	private MemRepository memRepository;

	// 新增
	public void addMemProCpn(MemProCpnVO memProCpnVO) {
		memProCpnRepository.save(memProCpnVO);
	}

	// 修改
	public void updateMemProCpn(MemProCpnVO memProCpnVO) {
		memProCpnRepository.save(memProCpnVO);
	}

	// 查全部
	public List<MemProCpnVO> getAll() {
		return memProCpnRepository.findAll();
	}

//	  /**
//     * 查詢會員的所有折價券（含已使用/過期）
//     */
//    public List<MemProCpnVO> getCouponsByMember(Integer memId) {
//        return memProCpnRepo.findByMemVO_MemIdOrderByCrtAtDesc(memId);
//    }
	// 查「某會員」未使用且有效折價券
	public List<MemProCpnVO> getValidCpnsByMember(Integer memId) {
		return memProCpnRepository.findValidCpnByMember(memId);
	}

	public void giveRegisterCoupon(Integer memId) {

		// ✅ 查出註冊折價券（is_active=1）
		ProCpnVO coupon = proCpnRepository.findByCpnNameAndIsActive("新客專屬9折券", IsActive.ACTIVE)
				.orElseThrow(() -> new RuntimeException("沒有啟用中的註冊折價券"));
		// ✅ 查出該會員（保證存在）
		Mem mem = memRepository.findById(memId).orElseThrow(() -> new RuntimeException("找不到該會員：" + memId));

		// ✅ 建立會員折價券關聯紀錄
		MemProCpnVO memCpn = new MemProCpnVO();
		memCpn.setMemVO(mem);
		memCpn.setProCpnVO(coupon);
		memCpn.setCpnUseStatus((byte) 0); // 0=未使用
		memCpn.setRcvAt(LocalDateTime.now());
		memCpn.setCrtAt(LocalDateTime.now());
		memCpn.setEffStart(LocalDate.now());

		// 設定有效期限（用 valid_days）
		if (coupon.getValidDays() != null) {
			memCpn.setEffEnd(LocalDate.now().plusDays(coupon.getValidDays()));
		} else {
			memCpn.setEffEnd(LocalDate.now().plusDays(30)); // 沒設定就給預設30天
		}

		// ✅ 儲存
		memProCpnRepository.save(memCpn);
	}

//發放生日折價券（每天執行）	
//	public void giveBirthdayCoupons() {
//		// 今天日期（不含時間）
//		LocalDate today = LocalDate.now();
//		// 找出今天生日的會員
//		List<Mem> birthdayMems = memRepository.findByMemBirthMonthAndDay(today.getMonthValue(), today.getDayOfMonth());
//
//		// 找出生日券
//		ProCpnVO birthdayCpn = proCpnRepository.findByCpnNameAndIsActive("生日快樂折價券", IsActive.ACTIVE)
//				.orElseThrow(() -> new RuntimeException("找不到啟用中的生日券！"));
//
//		for (Mem mem : birthdayMems) {
//			// 避免重複發券
//			boolean alreadyHas = memProCpnRepository.existsByMemVO_MemIdAndProCpnVO_ProCpnId(mem.getMemId(),
//					birthdayCpn.getProCpnId());
//			if (alreadyHas)
//				continue;
//
//			MemProCpnVO vo = new MemProCpnVO();
//			vo.setMemVO(mem);
//			vo.setProCpnVO(birthdayCpn);
//			vo.setCpnUseStatus((byte) 0);
//			vo.setRcvAt(LocalDateTime.now());
//			vo.setEffStart(LocalDate.now());
//			vo.setEffEnd(LocalDate.now().plusDays(birthdayCpn.getValidDays()));
//			memProCpnRepository.save(vo);
//		}
//	}

}