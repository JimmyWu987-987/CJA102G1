// com.farmtastic.proorder.model.ProOrderSevice.java

package com.farmtastic.proorder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.member.model.Mem;
import com.farmtastic.proorderitem.model.ProOrderItemId;
import com.farmtastic.proorderitem.model.ProOrderItemRepository;
import com.farmtastic.proorderitem.model.ProOrderItemVO;
import com.farmtastic.shoppingcart.model.Product;
import com.farmtastic.shoppingcart.model.ProductService;

@Service
public class ProOrderSevice {

	@Autowired
	ProOrderRepository repository;
	@Autowired
	ProOrderItemRepository proOrderItemRepository;
	@Autowired
	ProductService productSvc;

	// 新增
	@Transactional
	public void addProOrder(ProOrderVO proOrderVO, List<ProOrderItemVO> originalItems) {
	    // 🌟 修正步驟 1: 暫時移除訂單明細，避免級聯儲存 (cascade) 失敗
	    // 因為 originalItems 是從 Session 來的，裡面的 ProOrderItemVO 都沒有設定複合主鍵 ID。
	    // 如果直接儲存 proOrderVO，JPA 會因級聯關係嘗試儲存這些沒有 ID 的明細而報錯。
	    // 透過設定為 null，我們告訴 JPA：「先別管明細，專心儲存主訂單就好」。
	    proOrderVO.setProOrderItems(null);

	    // 步驟 2: 先儲存主訂單 (ProOrderVO)，這樣 JPA 才會為我們產生 proOrdId
	    repository.save(proOrderVO);
	    
	    // 步驟 3: 從剛剛儲存的 proOrderVO 物件中，取得自動生成的主鍵 proOrdId
	    Integer generatedOrdId = proOrderVO.getProOrdId(); 

	    // 步驟 4: 創建一個新的、真正要寫入資料庫的明細列表
	    List<ProOrderItemVO> newItemsToSave = new ArrayList<>();

	    // 步驟 5: 遍歷從購物車傳來的原始訂單明細 (originalItems)
	    for (ProOrderItemVO oldItem : originalItems) {
	        
	        // A. 獲取商品 ID
	        Product sessionProduct = oldItem.getProductVO();
	        Integer proId = null;
	        
	        if (sessionProduct != null) {
	            proId = sessionProduct.getProId();
	        }
	        
	        if (proId == null) {
	            throw new RuntimeException("訂單明細中缺少商品ID，無法新增訂單。");
	        }
	        
	        // B. 從資料庫重新載入 Product 實體，確保它是受 JPA 管理的狀態 (Attached)
	        Product attachedProduct = productSvc.getProductById(proId);
	        
	        // C. 創建一個全新的 ProOrderItemVO 物件來代表要儲存的訂單明細
	        ProOrderItemVO newItem = new ProOrderItemVO();
	        
	        // ✅ D. 【核心】手動創建並設定複合主鍵 (ProOrderItemId)
	        ProOrderItemId itemId = new ProOrderItemId(proId, generatedOrdId);
	        newItem.setId(itemId);
	        
	        // E. 設定關聯實體
	        newItem.setProductVO(attachedProduct); // 關聯至資料庫中的 Product
	        newItem.setProOrderVO(proOrderVO);     // 關聯回剛剛儲存的主訂單
	        
	        // F. 複製其他屬性 (單價、數量、小計)
	        newItem.setProUnitPrice(oldItem.getProUnitPrice());
	        newItem.setProAmount(oldItem.getProAmount());
	        newItem.setProSubTotal(oldItem.getProSubTotal());
	        
	        // G. 將這個準備好的新明細加入到待儲存列表中
	        newItemsToSave.add(newItem);
	    }

	    // 步驟 6: (可選，但建議) 將處理好 ID 的新明細列表設定回 ProOrderVO 物件中，
	    // 讓這個 Java 物件的狀態與資料庫保持同步。
	    proOrderVO.setProOrderItems(newItemsToSave);

	    // 步驟 7: 最後，一次性地將所有準備好的訂單明細儲存到資料庫
	    proOrderItemRepository.saveAll(newItemsToSave);
	}


	// 修改
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
	public ProOrderVO getOneProOrder(Integer proOrdId) {
		Optional<ProOrderVO> optional = repository.findById(proOrdId);
		return optional.orElse(null);
	}

	// 一般會員查自己的全部訂單
	public List<ProOrderVO> getAllByMemId(Mem MemVo) {
		return repository.findByMemVO(MemVo);
	}

	// 小農fmem查詢自己的全部表單
	public List<FmemOrderSummary> getAllByFmemId(Integer fmemId) {
		return repository.findFmemProOrders(fmemId);
	}
}