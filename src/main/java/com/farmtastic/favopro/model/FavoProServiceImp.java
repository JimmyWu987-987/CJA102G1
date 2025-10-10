package com.farmtastic.favopro.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.member.model.MemRepository;
import com.farmtastic.shoppingcart.model.ProductRepository;

import jakarta.transaction.Transactional;

@Service("favoProService")
public class FavoProServiceImp {
	@Autowired
	FavoProRepository favoRepository;

	@Autowired
	private MemRepository memRepository;
	@Autowired // 🌟 注入真正的 Repository 🌟
	private ProductRepository productRepository;

//private SessionFactory sessionFactory;
	// 新增收藏
	public void addFavoPro(Integer memId, Integer proId) {
		FavoProId favoProId = new FavoProId(memId, proId);
		if (favoRepository.existsById(favoProId)) {
			throw new IllegalStateException("該會員已收藏此商品");
		}
		FavoProVO favo = new FavoProVO();
		favo.setId(favoProId);
		favo.setMemVO(memRepository.findById(memId).orElseThrow());
		favo.setProductVO(productRepository.findById(proId).orElseThrow());
		favoRepository.save(favo);
	}

	// 取消收藏
	@Transactional
	public void removeFavoPro(Integer memId, Integer proId) {
		FavoProId id = new FavoProId(memId, proId);
		if (!favoRepository.existsById(id)) {
			throw new IllegalArgumentException("收藏紀錄不存在: " + id);
		}
		favoRepository.deleteById(id);
	}

	// 檢查是否已收藏
	public boolean isFavorite(Integer memId, Integer proId) {
		return favoRepository.existsById(new FavoProId(memId, proId));
	}

	public List<FavoProVO> getAll() {
		return favoRepository.findAll();
	}

	// 查詢指定會員的所有收藏清單
	public List<FavoProVO> getByMember(Integer memId) {
		return favoRepository.findByMemVO_MemId(memId);
	}

}
