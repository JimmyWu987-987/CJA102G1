package com.farmtastic.favopro.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoProRepository extends JpaRepository<FavoProVO, FavoProId> {
	// 檢查指定的收藏紀錄是否存在
	boolean existsById(FavoProId id);

	// 查出「指定會員」收藏的所有商品。
	List<FavoProVO> findByMemVO_MemId(Integer memId);

	// 刪除指定主鍵的收藏紀錄
	void deleteById(FavoProId id);
}
