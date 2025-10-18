package com.farmtastic.favoact.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoActRepository extends JpaRepository<FavoActVO, FavoActId> {
	// 檢查指定的收藏紀錄是否存在
	boolean existsById(FavoActId id);

	// 查出「指定會員」收藏的所有活動。
	@Query("SELECT f FROM FavoActVO f JOIN FETCH f.actVO WHERE f.memVO.memId = :memId")
	List<FavoActVO> findByMemVO_MemId(@Param("memId") Integer memId);

	// 刪除指定主鍵的收藏紀錄
	void deleteById(FavoActId id);

}
