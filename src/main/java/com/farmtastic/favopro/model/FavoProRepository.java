package com.farmtastic.favopro.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//Repository 介面，用來存取 FavoProVO 資料表，主鍵是 FavoProId（複合主鍵）
@Repository
public interface FavoProRepository extends JpaRepository<FavoProVO, FavoProId> {
	// 檢查指定的收藏紀錄是否存在
	boolean existsById(FavoProId id);

	// 撈出某商品的第一張圖片
	// ProImage findFirstByProIdOrderByProImgIdAsc(Long proId);

	// 查出「指定會員」收藏的所有商品。
	// 同時載商品資料
	@Query("SELECT f FROM FavoProVO f JOIN FETCH f.productVO WHERE f.memVO.memId = :memId")
	List<FavoProVO> findByMemVO_MemId(@Param("memId") Integer memId);

	// 刪除指定主鍵的收藏紀錄
	void deleteById(FavoProId id);
}
