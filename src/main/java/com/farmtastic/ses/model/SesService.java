package com.farmtastic.ses.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SesService {
	
	@Autowired
	private SesRepository sesRepository;
	
	
	// ========== 新增場次 ==========
	public void addSes(Ses ses) {
		sesRepository.save(ses);
	}

	// ========== 修改場次 (編輯. 上下架) ==========
	public void updateAct(Ses ses) {
		sesRepository.save(ses);
	}
	
	
	
// 查詢
	
	// ========== 依活動ID查詢 ==========
	public List<Ses> findByActId(Integer actId, Sort sort) {
		return sesRepository.findByActId(actId, sort);
	}
	
	// ========== 依場次ID查單一場次 ==========
	public Ses getOneSes(Integer sesId) {
		return sesRepository.findById(sesId).orElse(null);
	}

	// ========== 依場次上下架狀態查詢 ==========
	public List<Ses> findBySesLaunStat(Integer sesLaunStat, Sort sort) {
		return sesRepository.findBySesLaunStat(sesLaunStat, sort);
	}
	
	// ========== 依報名狀態查詢 ==========
	public List<Ses> findByActStat(Integer actStat, Sort sort) {
		return sesRepository.findByRegStat(actStat, sort);
	}

	// ========== 查全部場次 ==========
	public List<Ses> getAllSes(Sort sort) {
		return sesRepository.findAll(sort);
	}

	
	
	
	
	
	// ========== 刪除場次 ==========
	public void deleteAct(Integer sesId) {
		if (sesRepository.existsById(sesId)) {
			sesRepository.deleteBySesId(sesId);
		}
	}
	
	
	
}