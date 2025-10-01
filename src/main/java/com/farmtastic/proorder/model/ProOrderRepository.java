package com.farmtastic.proorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmtastic.member.model.Mem;

@Repository
public interface ProOrderRepository extends JpaRepository<ProOrderVO,Integer>{
	
	// 一般會員查自己的全部訂單
	List<ProOrderVO> findByMemVO(Mem memVO);
	
	// 小農查詢自己的全部表單
	// 小農查詢該會員有幾筆訂單
	// 小農查詢該商品有幾筆訂單
	// 後台查詢該小農商品有幾筆訂單（回傳多筆）
	// 用復合查詢？
	
}
