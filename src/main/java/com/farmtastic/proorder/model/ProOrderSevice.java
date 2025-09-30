package com.farmtastic.proorder.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProOrderSevice {

		@Autowired
		ProOrderRepository repository;
		
//		@Autowired
//		private SessionFactory sessionFactory;
		
		// 新增
		public void addProOrder(ProOrderVO proOrderVO) {
			repository.save(proOrderVO);
		}
		// 修改
		public void updateProOrder(ProOrderVO proOrderVO) {
			repository.save(proOrderVO);
		}
		// 刪除
		public void deleteProOrder(Integer proOrdId) {
			if(repository.existsById(proOrdId)) {
				repository.deleteById(proOrdId);
			}
		}
		// 查全部
		public List<ProOrderVO> getAll(){
			return repository.findAll();
		}
		
		// 訂單單一查詢
		public ProOrderVO getOneProOrder(Integer proOrdId) {
			Optional<ProOrderVO> optional = repository.findById(proOrdId);
			return optional.orElse(null);
		}
		
		
		// 小農fmem查詢自己的全部表單
		// 小農fmem查詢該會員有幾筆訂單
		// 小農查詢該商品有幾筆訂單
		// 後台查詢該小農商品有幾筆訂單（回傳多筆）
		// 用復合查詢？
		
	
}
