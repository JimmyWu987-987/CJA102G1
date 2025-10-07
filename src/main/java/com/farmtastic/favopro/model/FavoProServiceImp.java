package com.farmtastic.favopro.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service("favoProService")
public class FavoProServiceImp {
	@Autowired
	FavoProRepository repository;

//private SessionFactory sessionFactory;

	public void addFavoPro(Integer proId, Integer memId) {
		FavoProId favoProId = new FavoProId(proId, memId);
		FavoProVO favo = new FavoProVO();
		favo.setId(favoProId);
		repository.save(favo);
	}

	// 取消收藏（刪除）
	@Transactional
	public void removeFavoPro(FavoProId favoProId) {
		if (!repository.existsById(favoProId)) {
			throw new IllegalArgumentException("收藏紀錄不存在: " + favoProId);
		}
		repository.deleteById(favoProId);
	}

	public List<FavoProVO> getAll() {
		return repository.findAll();
	}

	public FavoProVO getOneFavoPro(Integer proId, Integer memId) {
		FavoProId favoProId = new FavoProId(proId, memId);
		Optional<FavoProVO> optional = repository.findById(favoProId);
		return optional.orElse(null);
	}

}
