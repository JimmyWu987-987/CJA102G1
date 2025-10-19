package com.farmtastic.favopro.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//收藏快取版
@Transactional
@Service
public class FavoCacheService {

	private final Set<FavoProId> favoriteCache = new HashSet<>();

	// 初始化時把資料庫裡的收藏載入快取
	@Autowired
	public FavoCacheService(FavoProRepository repo) {
		List<FavoProVO> all = repo.findAll();
		for (FavoProVO f : all) {
			favoriteCache.add(new FavoProId(f.getMemVO().getMemId(), f.getProductVO().getProId()));
		}
	}

	// 檢查是否已收藏
	public boolean isFavorite(Integer memId, Integer proId) {
		return favoriteCache.contains(new FavoProId(memId, proId));
	}

	// 新增收藏（同步快取）
	public void addFavorite(Integer memId, Integer proId) {
		favoriteCache.add(new FavoProId(memId, proId));
	}

	// 移除收藏
	public void removeFavorite(Integer memId, Integer proId) {
		favoriteCache.remove(new FavoProId(memId, proId));
	}

	public Set<FavoProId> getAllFavorites() {
		return favoriteCache;
	}

}
