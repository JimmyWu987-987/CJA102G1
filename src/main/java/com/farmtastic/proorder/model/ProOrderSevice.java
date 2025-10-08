package com.farmtastic.proorder.model;

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

//		@Autowired
//		private SessionFactory sessionFactory;

	// 新增
	@Transactional
	public void addProOrder(ProOrderVO proOrderVO, List<ProOrderItemVO> proOrderItemVO) {
		// 1. 先儲存 ProOrderVO，讓 JPA 自動產生 proOrdId
		repository.save(proOrderVO);

		// 2. 遍歷訂單明細，設定好關聯實體和複合主鍵
		for (ProOrderItemVO item : proOrderItemVO) {
			
			// 🌟 步驟 A: 實例化複合主鍵 🌟
			if (item.getId() == null) {
				item.setId(new ProOrderItemId());
			}

			// 🌟 步驟 B: 獲取商品 ID 並進行嚴格檢查 🌟
			// 獲取從 Session 來的 ProOrderItemVO 中 ProductVO 裡面的 proId
			Product sessionProduct = item.getProductVO();
			Integer proId = null;
			
			if (sessionProduct != null) {
			    proId = sessionProduct.getProId();
			}
			
			// 如果商品 ID 無效，則拋出異常，防止 NulPointerException
			if (proId == null) {
			    throw new RuntimeException("訂單明細中缺少商品ID，無法新增訂單。");
			}
			
			// 🌟 步驟 C: 從 DB 載入 Attached 實體 🌟
			// 這是為了替換掉 Detached Entity，讓 @MapsId 能夠正確運作
			Product attachedProduct = productSvc.getProductById(proId);
			
			// 🌟 步驟 D: 設定關聯實體 🌟
			
			// 1. 設定 Product 關聯 (用於 proId)
			item.setProductVO(attachedProduct); 

			// 2. 設定 ProOrderVO 關聯 (用於 proOrdId)
			item.setProOrderVO(proOrderVO);
			
			// (不需要手動設定 item.getId().setProId 和 item.getId().setProOrdId，
			//  因為 @MapsId 會自動從 attachedProduct 和 proOrderVO 中獲取)
		}

		// 4. 將更新後的明細集合設定回 ProOrderVO (確保物件狀態完整)
		proOrderVO.setProOrderItems(proOrderItemVO);

		// 5. 儲存所有訂單明細 (透過明細 Repository 儲存，觸發 ID 寫入)
		proOrderItemRepository.saveAll(proOrderItemVO);
	}

	// 修改
	public void updateProOrder(ProOrderVO proOrderVO) {
		repository.save(proOrderVO);
	}

	// 刪除
	public void deleteProOrder(Integer proOrdId) {
		if (repository.existsById(proOrdId)) {
			// 當執行 deleteById 時，JPA 會因為 ProOrderVO 設定的 cascade=ALL
			// 而自動刪除所有關聯的 ProOrderItemVO。
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
	// 小農fmem查詢該會員有幾筆訂單
	// 小農查詢該商品有幾筆訂單
	// 後台查詢該小農商品有幾筆訂單（回傳多筆）
	// 用復合查詢？

}
