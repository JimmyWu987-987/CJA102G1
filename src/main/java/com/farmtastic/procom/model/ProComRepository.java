package com.farmtastic.procom.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;

@Repository
public interface ProComRepository extends JpaRepository<ProComVO, Integer> {
	
	// 查詢該商品的所有評論
	List<ProComVO> findByProVO(Pro proVO);
	
	// 查詢該小農的所有評論
	// 功能在 ProComService.java 內 
	
	// 計算該商品的總分數
	// 功能在 ProComService.java 內 
	
	// 查詢該會員的所有評論
	List<ProComVO> findByMemVO(Mem memVO);
	
}
