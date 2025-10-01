package com.farmtastic.proorderitem.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmtastic.proorder.model.ProOrderVO;

public interface ProOrderItemRepository extends JpaRepository<ProOrderItemVO, ProOrderItemId>{ // 注意：主鍵類型應為 ProOrderItemId

	// 查詢指定訂單的全部訂單明細
	List<ProOrderItemVO> findByProOrderVO(ProOrderVO proOrderVO);
}
