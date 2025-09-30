package com.farmtastic.proorder.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProOrderRepository extends JpaRepository<ProOrderVO,Integer>{

	// 小農查詢自己的全部表單
	// 小農查詢該會員有幾筆訂單
	// 小農查詢該商品有幾筆訂單
	// 後台查詢該小農商品有幾筆訂單（回傳多筆）
	// 用復合查詢？
	
}
