package com.farmtastic.proorderitem.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.proorder.model.ProOrderVO;


@Service
public class ProOrderItemService {
	
	@Autowired
	ProOrderItemRepository repository;
	
	// 新增
	public void addProOrderItem(ProOrderItemVO proOrderItemVO) {
		repository.save(proOrderItemVO);
	}
	// 修改
	public void updateProOrderItem(ProOrderItemVO proOrderVO) {
		repository.save(proOrderVO);
	}
	// 刪除
	public void deleteProOrderItem(Integer proOrdId) {

	}
	// 查全部
	public List<ProOrderItemVO> getAll(){
		return repository.findAll();
	}
	
	// 查詢指定訂單的全部訂單明細
	public List<ProOrderItemVO> getProOrderItems(ProOrderVO proOrderVO){
		return repository.findByProOrderVO(proOrderVO);
	}

}
