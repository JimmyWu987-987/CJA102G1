package com.farmtastic.actIndex.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActCate;


public interface ActIndexRepository extends JpaRepository<Act, Integer> {
	// 活動
	@Query("select a from Act a order by a.actStart desc")
    List<Act> findActForIndex();    
	// 活動分類
	@Query("select b from ActCate b")
    List<ActCate> findActCateForIndex();  
	// 活動主圖
	@Query("select a.actMainImg from Act a where a.actId = :id") 
	byte[] findImgById(@Param("id") Integer id);
}