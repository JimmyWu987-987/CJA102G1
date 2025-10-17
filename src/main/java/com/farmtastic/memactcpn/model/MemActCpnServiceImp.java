package com.farmtastic.memactcpn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.actcpn.model.ActCpnRepository;
import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.common.mapper.MemActCpnMapperImp;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;

import jakarta.transaction.Transactional;

@Service("MemActCpnService")
public class MemActCpnServiceImp {
	@Autowired
	MemActCpnRepository memActCpnRepository;
	@Autowired
	private ActCpnRepository actCpnRepository; // ✅ 查找折價券名稱用這個

	@Autowired
	private MemActCpnMapperImp mapper;
	@Autowired
	private MemRepository memRepository;

	// 新增
	public void addMemActCpn(MemActCpnVO memActCpnVO) {
		memActCpnRepository.save(memActCpnVO);
	}

	// 修改
	public void updateMemActCpn(MemActCpnVO memActCpnVO) {
		memActCpnRepository.save(memActCpnVO);
	}

	// 查全部
	public List<MemActCpnVO> getAll() {
		return memActCpnRepository.findAll();
	}

	// 查某張券的「已使用」清單
	public List<MemActCpnVO> getUsedRecords(Integer cpnId) {
		return memActCpnRepository.findUsedRecords(cpnId, CpnUseStatus.USED);
	}

	// 查「某會員」未使用且有效折價券
	public List<MemActCpnVO> getValidCpnsByMember(Integer memId) {
		return memActCpnRepository.findValidCpnByMember(memId);
	}

	// 查出一筆會員折價券
	public MemActCpnVO getOne(Integer cpnHolderDetailId) {
		return memActCpnRepository.findById(cpnHolderDetailId).orElse(null);
	}

	// 查「某會員」所有效折價券
	public List<MemActCpnVO> getCpnsByMember(Integer memId) {
		List<MemActCpnVO> list = memActCpnRepository.findAllByMember(memId);
		return list == null ? Collections.emptyList() : list;
	}

	// 發送券
	@Transactional
	public void giveCoupon(Integer memId, Integer actCpnId) {

		// 查出折價券規則
		ActCpnVO actCpnVO = actCpnRepository.findById(actCpnId)
				.orElseThrow(() -> new RuntimeException("找不到指定折價券ID：" + actCpnId));
		// 查出該會員（保證存在）
		Mem mem = memRepository.findById(memId).orElseThrow(() -> new RuntimeException("找不到該會員：" + memId));

		// 防重發
		if (memActCpnRepository.existsByMemVOAndActCpnVO(mem, actCpnVO)) {
			System.out.printf("⚠️ 會員 %d 已領取過【%s】，跳過%n", memId, actCpnVO.getCpnName());
			return;
		}

		// ✅ 建立會員折價券關聯紀錄
		MemActCpnVO memActCpn = new MemActCpnVO();
		memActCpn.setMemVO(mem);
		memActCpn.setActCpnVO(actCpnVO);
		memActCpn.setCpnUseStatus(CpnUseStatus.UNUSED); // 0=未使用
		memActCpn.setRcvAt(LocalDateTime.now());
		memActCpn.setCrtAt(LocalDateTime.now());
		memActCpn.setEffStart(LocalDate.now());

		// 設定有效期限（用 valid_days）
		if (actCpnVO.getValidDays() != null) {
			memActCpn.setEffEnd(LocalDate.now().plusDays(actCpnVO.getValidDays()));
		} else {
			memActCpn.setEffEnd(LocalDate.now().plusDays(30)); // 沒設定就給預設30天
		}

		// ✅ 儲存
		memActCpnRepository.save(memActCpn);
	}

}
