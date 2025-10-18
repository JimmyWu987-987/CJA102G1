package com.farmtastic.favopro.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.member.model.MemRepository;
import com.farmtastic.pro.model.ProRepository;

import jakarta.transaction.Transactional;

@Service("favoProService")
@Transactional
public class FavoProServiceImp {
	@Autowired
	FavoProRepository favoRepository;
	@Autowired
	MemRepository memRepository;
	@Autowired
	ProRepository proRepository;

//Hibernate 用SessionFactory 管理連線與交易
//private SessionFactory sessionFactory;
	// 新增收藏
	public void addFavoPro(Integer memId, Integer proId) {
		FavoProId favoProId = new FavoProId(memId, proId);

		if (favoRepository.existsById(favoProId)) {
			throw new IllegalStateException("該會員已收藏此商品");
		}
		FavoProVO favo = new FavoProVO();
		favo.setId(favoProId);
		// setMemVO 放入 Optional<Mem>
		favo.setMemVO(memRepository.findById(memId).orElseThrow());
		favo.setProductVO(proRepository.findById(proId).orElseThrow());
		favoRepository.save(favo);
	}

	// 取消收藏
	public void removeFavoPro(Integer memId, Integer proId) {
		FavoProId favoProId = new FavoProId(memId, proId);
		if (!favoRepository.existsById(favoProId)) {
			throw new IllegalArgumentException("收藏紀錄不存在: " + favoProId);
		}
		favoRepository.deleteById(favoProId);
	}

	// 檢查是否已收藏
	public boolean isFavorite(Integer memId, Integer proId) {
		return favoRepository.existsById(new FavoProId(memId, proId));
	}

	// 前台用：查詢某會員收藏清單
	public List<FavoProVO> getByMember(Integer memId) {
		return favoRepository.findByMemVO_MemId(memId);
	}

	// 後台管理用：查詢所有收藏紀錄(目前沒用到)
	public List<FavoProVO> getAll() {
		return favoRepository.findAll();
	}
}
