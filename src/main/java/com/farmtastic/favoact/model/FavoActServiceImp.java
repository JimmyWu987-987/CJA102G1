package com.farmtastic.favoact.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.act.model.ActRepository;
import com.farmtastic.member.model.MemRepository;

import jakarta.transaction.Transactional;

@Service("favoActService")
@Transactional
public class FavoActServiceImp {
	@Autowired
	FavoActRepository favoactRepository;
	@Autowired
	MemRepository memRepository;
	@Autowired
	ActRepository actRepository;

	// 新增收藏
	public void addFavoAct(Integer memId, Integer actId) {
		FavoActId favoActId = new FavoActId(memId, actId);

		if (favoactRepository.existsById(favoActId)) {
			throw new IllegalStateException("該會員已收藏此活動");
		}
		FavoActVO favo = new FavoActVO();
		favo.setId(favoActId);
		favo.setMemVO(memRepository.findById(memId).orElseThrow());
		favo.setActVO(actRepository.findById(actId).orElseThrow());
		favoactRepository.save(favo);
	}

	// 取消收藏
	public void removeFavoAct(Integer memId, Integer actId) {
		FavoActId favoActId = new FavoActId(memId, actId);
		if (!favoactRepository.existsById(favoActId)) {
			throw new IllegalArgumentException("收藏紀錄不存在: " + favoActId);
		}
		favoactRepository.deleteById(favoActId);
	}

	// 檢查是否已收藏
	public boolean isFavorite(Integer memId, Integer actId) {
		return favoactRepository.existsById(new FavoActId(memId, actId));
	}

	public List<FavoActVO> getByMember(Integer memId) {
		return favoactRepository.findByMemVO_MemId(memId);
	}

	// 查全部
	public List<FavoActVO> getAll() {
		return favoactRepository.findAll();
	}
}
