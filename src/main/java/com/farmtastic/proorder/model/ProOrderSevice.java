package com.farmtastic.proorder.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProOrderSevice {

		@Autowired
		ProOrderRepository repository;
		
//		@Autowired
//		private SessionFactory sessionFactory;
		
//		// 新增
//		public void addProOrder(ProOrderVO proOrderVO) {
//			repository.save(proOrderVO);
//		}
//		// 修改
//		public void updateProOrder(ProOrderVO proOrderVO) {
//			repository.save(proOrderVO);
//		}
//		// 刪除
//		public void deleteProOrder(Integer proOrdId) {
//			if(repository.existsById(proOrdId)) {
//				repository.deleteById(proOrdId);
//			}
//		}
		// 查全部
		public List<ProOrderVO> getAll(){
			return repository.findAll();
		}
		// 查單一
		
		// 查詢該會員有幾筆訂單
		// 查詢該商品有幾筆訂單
		// 查詢該小農商品有幾筆訂單（回傳多筆）
		// 用復合查詢？
		
	
}
