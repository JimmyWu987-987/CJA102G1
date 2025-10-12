package com.farmtastic.act.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActService {

	@Autowired
	private ActRepository actRepository;
	
	
	// ========== 新增活動 ==========
	public void addAct(Act act) {
		actRepository.save(act);
	}

	// ========== 修改活動 (編輯. 審核. 上下架) ==========
	public void updateAct(Act act) {
		actRepository.save(act);
	}
	
	// ========== 依活動ID查單一活動 ==========
	public Act getOneAct(Integer actId) {
		return actRepository.findById(actId).orElse(null);
    }

    // ========== 查全部活動 ==========
	public List<Act> getAllAct(Sort sort) {
		return actRepository.findAll(sort);
	}

	// ========== 依小農ID查詢 ==========
	public List<Act> findByFmemId(Integer fmemId, Sort sort) {
		return actRepository.findByFmemId(fmemId, sort);
	}
	
	// ========== 依審核狀態查詢 ==========
	public List<Act> findByActStat(Integer actStat, Sort sort) {
		return actRepository.findByActStat(actStat, sort);
	}
	
	// ========== 依活動上下架狀態查詢 ==========
	public List<Act> findByActLaunStat(Integer launStat, Sort sort) {
		return actRepository.findByActLaunStat(launStat, sort);
	}

	// ========== 複合查詢所有活動（動態排序） ==========
	public List<Act> findActByCQ(Integer fmemId, Integer actStat, Integer actLaunStat,
								 Integer actCateId, String keyword, Sort sort) {
		return actRepository.findActByCQ(fmemId, actStat, actLaunStat, actCateId, keyword, sort);
	}	

	// ========== 刪除活動 ==========
	public void deleteAct(Integer actId) {
		if (actRepository.existsById(actId)) {
			actRepository.deleteByActId(actId);
		}
	}
	
	
}