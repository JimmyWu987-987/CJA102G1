// com.farmtastic.proorder.model.ProOrderSevice.java

package com.farmtastic.proorder.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proorderitem.model.ProOrderItemRepository;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

@Service
public class ProOrderSevice {

	@Autowired
	ProOrderRepository repository;
	@Autowired
	ProOrderItemRepository proOrderItemRepository;
	@Autowired
	ProService productSvc;

	// 新增
	@Transactional
	public void addProOrder(ProOrderVO proOrderVO) {
		  // 🌟 關鍵修正：將脫管的 Product 實體轉換為受管實體 🌟
	    if (proOrderVO.getProOrderItems() != null) {
	        for (ProOrderItemVO item : proOrderVO.getProOrderItems()) {
	            // 1. 取得脫管 Product 的 ID
	            Integer proId = item.getProductVO().getProId(); 
	            
	            // 2. 從資料庫中重新載入 Product 實體 (受管)
	            // 假設 productSvc.getOneProduct(proId) 會回傳 Product 實體
	            Pro managedProduct = productSvc.getOnePro(proId);
	            
	            if (managedProduct == null) {
	                // 如果找不到商品，則拋出錯誤
	                throw new RuntimeException("商品編號 " + proId + " 不存在，無法新增訂單明號。");
	            }
	            
	            // 3. 將脫管的 Product 實體替換為受管實體
	            item.setProductVO(managedProduct);
	            
	            // 4. 由於您在 Controller 中已設定複合主鍵，此處保持不變。
                // 確保明細指向當前訂單 (雙向關聯)，雖然在 Controller 中已設定，但多做一次確保
	            item.setProOrderVO(proOrderVO); 
	        }
	    }
	    
	    // 執行儲存操作，現在所有關聯的 Product 都是受管實體，不會報錯。
	    repository.save(proOrderVO);
	}


	// 修改
	@Transactional
	public void updateProOrder(ProOrderVO proOrderVO) {
		repository.save(proOrderVO);
	}

	// 刪除
	public void deleteProOrder(Integer proOrdId) {
		if (repository.existsById(proOrdId)) {
			repository.deleteById(proOrdId);
		}
	}

	// 查全部
	public List<ProOrderVO> getAll() {
		return repository.findAll();
	}

	// 訂單編號的單一查詢
	@Transactional
	public ProOrderVO getOneProOrder(Integer proOrdId) {
		Optional<ProOrderVO> optional = repository.findById(proOrdId);
		return optional.orElse(null);
	}

	// 一般會員查自己的全部訂單
	@Transactional
	public List<ProOrderVO> getAllByMemId(Mem MemVo) {
		return repository.findByMemVO(MemVo);
	}

	// 小農fmem查詢自己的全部表單
	@Transactional
	public List<FmemOrderSummary> getAllByFmemId(Integer fmemId) {
		return repository.findFmemProOrders(fmemId);
	}
	
	// 查詢該小農“已到貨”以及“已退貨的”全部訂單，可以撥款的訂單
	@Transactional
	public List<FmemOrderSummary> getAllByFmemIdCanAlloc(Integer fmemId) {
		return repository.findFmemProOrdersCanAlloc(fmemId);
	}
}