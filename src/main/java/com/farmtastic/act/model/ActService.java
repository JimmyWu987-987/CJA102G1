package com.farmtastic.act.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
	
	// ========== 依活動ID查單一活動 (也撈圖) ==========
	@Transactional(readOnly = true)
	public Optional<Act> getOneAct(Integer actId) {
	    return actRepository.findByActIdWithImgs(actId);
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
	
	//	for 小農
	public List<Act> findActByCQForFmem(Integer fmemId, Integer actStat, Integer actLaunStat,
								 Integer actCateId, String keyword, Sort sort) {
		return actRepository.findActByCQForFmem(fmemId, actStat, actLaunStat, actCateId, keyword, sort);
	}
	//	for 消費者 (不篩小農)
	public List<Act> findActByCQForCus(Integer actCateId, String keyword, Sort sort) {
		return actRepository.findActByCQForCus(actCateId, keyword, sort);
	}
	
	//	for 後台
	public List<Act> findActByCQForAdmin(Integer fmemId, Integer actStat, Integer actLaunStat,
								 Integer actCateId, String keyword, Sort sort) {
		return actRepository.findActByForAdmin(fmemId, actStat, actLaunStat, actCateId, keyword, sort);
	}
	
	

	// ========== 刪除活動 ==========
	public void deleteAct(Integer actId) {
		if (actRepository.existsById(actId)) {
			actRepository.deleteByActId(actId);
		}
	}

	// ========== 挖活動圖片 ===========
	@Transactional
	public List<byte[]> getAllActImagesForCarousel(Integer actId) {
	    Act act = actRepository.findById(actId).orElse(null);
	    if (act == null) {
	        return List.of(); // 找不到活動就回空列表
	    }

	    List<byte[]> imgList = new ArrayList<>();

	    // 主圖先加進列表
	    if (act.getActMainImg() != null) {
	        imgList.add(act.getActMainImg());
	    }

	    // 初始化 Lazy 的活動圖片集合
	    act.getActImg().size();

	    // 依照 actImgOrder 排序後加進列表
	    act.getActImg().stream()
	    .sorted(Comparator.comparing(ActImg::getActimgOrder))
	    .forEach(a -> imgList.add(a.getActImg()));

	    return imgList;
	}
}