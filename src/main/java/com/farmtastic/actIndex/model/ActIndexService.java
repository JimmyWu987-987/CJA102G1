package com.farmtastic.actIndex.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActCate;

@Service("actIndexService")
public class ActIndexService {
	@Autowired
	ActIndexRepository actIndexRepository;

	// 活動首頁顯示活動列表
	@Transactional(readOnly = true)
	public List<Act> getAllForIndex() {
		return actIndexRepository.findActForIndex();
	}

	// 活動首頁顯示活動分類
	@Transactional(readOnly = true)
	public List<ActCate> getAllCateForIndex() {
		return actIndexRepository.findActCateForIndex();
	}

	// 活動主圖
	@Transactional(readOnly = true)
	public byte[] getMainImgById(Integer actId) {
		return actIndexRepository.findImgById(actId);

	}

}
