package com.farmtastic.memprocpn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.common.mapper.MemProCpnMapperImp;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

import jakarta.transaction.Transactional;

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

//修改卷狀態
	// cpnHolderDetailId 前端購物車選的折價券
	public void changeMemProCpnStatus(Integer memId, Integer cpnHolderDetailId) {
		// 查出這筆券
		MemProCpnVO vo = memProCpnRepository.findByMemVO_MemIdAndProCpnVO_ProCpnId(memId, cpnHolderDetailId)
				.orElseThrow(() -> new RuntimeException("找不到會員折價券記錄"));

		// 更新使用狀態
		vo.setCpnUseStatus(CpnUseStatus.USED); // 1 = 已使用
		// vo.setProOrdVO(orderVO); // 綁定訂單
		vo.setUsedAt(LocalDateTime.now()); // 使用時間

		// 儲存更新
		memProCpnRepository.save(vo);
	}

	// 查全部
	public List<MemProCpnVO> getAll() {
		return memProCpnRepository.findAll();
	}

// 查出一筆會員折價券
	public MemProCpnVO getOne(Integer cpnHolderDetailId) {
		return memProCpnRepository.findById(cpnHolderDetailId).orElse(null);
	}

	// 查「某會員」所有效折價券
	public List<MemProCpnVO> getCpnsByMember(Integer memId) {
		List<MemProCpnVO> list = memProCpnRepository.findAllByMember(memId);
		return list == null ? Collections.emptyList() : list.stream().filter(Objects::nonNull).toList();
	}

	// 查「某會員」未使用且有效折價券
	public List<MemProCpnVO> getValidCpnsByMember(Integer memId) {
		return memProCpnRepository.findValidCpnByMember(memId);
	}

// 發送券
	@Transactional
	public void giveCoupon(Integer memId, Integer proCpnId) {

		// 查出折價券規則
		ProCpnVO proCpnVO = proCpnRepository.findById(proCpnId)
				.orElseThrow(() -> new RuntimeException("找不到指定折價券ID：" + proCpnId));
		// 查出該會員（保證存在）
		Mem mem = memRepository.findById(memId).orElseThrow(() -> new RuntimeException("找不到該會員：" + memId));

		// 防重發
		if (memProCpnRepository.existsByMemVOAndProCpnVO(mem, proCpnVO)) {
			System.out.printf("⚠️ 會員 %d 已領取過【%s】，跳過%n", memId, proCpnVO.getCpnName());
			return;
		}

		// ✅ 建立會員折價券關聯紀錄
		MemProCpnVO memProCpn = new MemProCpnVO();
		memProCpn.setMemVO(mem);
		memProCpn.setProCpnVO(proCpnVO);
		memProCpn.setCpnUseStatus(CpnUseStatus.UNUSED); // 0=未使用
		memProCpn.setRcvAt(LocalDateTime.now());
		memProCpn.setCrtAt(LocalDateTime.now());
		memProCpn.setEffStart(LocalDate.now());

		// 設定有效期限（用 valid_days）
		if (proCpnVO.getValidDays() != null) {
			memProCpn.setEffEnd(LocalDate.now().plusDays(proCpnVO.getValidDays()));
		} else {
			memProCpn.setEffEnd(LocalDate.now().plusDays(30)); // 沒設定就給預設30天
		}

		// ✅ 儲存
		memProCpnRepository.save(memProCpn);
	}
}