package com.farmtastic.ses.model;

import java.util.List;
import java.util.Optional;

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
	public void updateSes(Ses ses) {
		sesRepository.save(ses);
	}
	
	
	
// 查詢

	// ========== 查小農自己的全部場次 ==========
	public List<Ses> findSesByFmemId(Integer fmemId, Sort sort) {
		return sesRepository.findByAct_Fmem_FmemId(fmemId, sort);
	}
	
	// ========== 依活動ID查詢 ==========
	public List<Ses> findSesByActId(Integer actId, Sort sort) {
		return sesRepository.findByActId(actId, sort);
	}
	
	// ========== 依場次ID查單一場次 ==========
	@Transactional(readOnly = true)
	public Optional<Ses> getOneSes(Integer sesId) {
		return sesRepository.findBySesId(sesId);
	}

	// ========== 依場次上下架狀態列出場次 >> for 上下架場次用 ==========
	public List<Ses> findBySesLaunStat(Integer sesLaunStat, Sort sort) {
		return sesRepository.findBySesLaunStat(sesLaunStat, sort);
	}
	
	// ========== 依報名狀態列出場次 >> ex: 供小農查看已成團的場次 ==========
	public List<Ses> findByRegStat(Integer actStat, Sort sort) {
		return sesRepository.findByRegStat(actStat, sort);
	}
	
	
	
	// ========== 查全部場次 (好像不會用到...) ==========
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