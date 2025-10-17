// com.farmtastic.proorder.model.ProOrderSevice.java

package com.farmtastic.proorder.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.model.MemProCpnRepository;
import com.farmtastic.memprocpn.model.MemProCpnVO;
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
	MemProCpnRepository mpcRepository;
	@Autowired
	ProService productSvc;

	// 每筆訂單的抽成百分筆
	private static final double ALLOC_PER = 0.1;

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


	
	
	//	====================================訂單一般會員前台使用====================================
	// 新增訂單的扣商品庫存的邏輯
	@Transactional
	public Pro discProductStock(ProOrderVO proOrderVO) {
		List<ProOrderItemVO> finalItems = proOrderVO.getProOrderItems();
		
		for (ProOrderItemVO itemList : finalItems) {
			// 查詢該產品的庫存
			Pro proVO = productSvc.getOnePro(itemList.getProductVO().getProId());
			Integer originalStock = proVO.getProStock();
			Integer discStock = itemList.getProAmount();
			Integer finalStock = originalStock - discStock;

			if (finalStock < 0) {
				return proVO;
			} else {
				proVO.setProStock(finalStock);
				productSvc.updatePro(proVO);
			}
		}
		return null;
	}
	
	@Transactional
	// 取消訂單返回庫存的邏輯
	public void cancelOrderAndBackStock(ProOrderVO proOrderVO) {
		
		List<ProOrderItemVO> finalItems = proOrderVO.getProOrderItems();
		
		
		for (ProOrderItemVO itemList : finalItems) {
			// 查詢該產品的庫存
			Pro proVO = productSvc.getOnePro(itemList.getProductVO().getProId());
			Integer originalStock = proVO.getProStock();
			Integer addStock = itemList.getProAmount();
			Integer finalStock = originalStock + addStock;

			proVO.setProStock(finalStock);
			productSvc.updatePro(proVO);

		}
	}
	@Transactional
	// 檢查是否有使用折價劵
	public boolean checkUseMcpn(ProOrderVO proOrderVO,Integer cpnHolderDetailId) {
		
		if (proOrderVO.getMemProCpnVO() != null) {

			// cpnHolderDetailId 不為 null 且不為 0 才表示有使用優惠券
			if (cpnHolderDetailId != null && cpnHolderDetailId != 0) {

					return true;
				
			} else {
				// cpnHolderDetailId 為 0 或 null，表示沒有使用優惠券
				return false;
			}
		} else {
			// memProCpnVO 為 null，表示沒有使用優惠券
			return false;
		}
	}

	
	//	====================================訂單後台使用====================================
	
	// 查詢該小農“已到貨”以及“已退貨的”全部訂單，可以撥款的訂單
	@Transactional
	public List<FmemOrderSummary> getAllByFmemIdCanAlloc(Integer fmemId) {
		return repository.findFmemProOrdersCanAlloc(fmemId);
	}
	
	// 訂單後台 - 修改訂單為已撥款狀態
	public void updateAllocStatus(Integer proOrdId) {
		ProOrderVO proOrderVO = getOneProOrder(proOrdId);

		if (proOrderVO.getProOrdAllocStatus() == 0) {
			proOrderVO.setProOrdAllocStatus((byte) 1);

			updateProOrder(proOrderVO);
		}
	}

	// 訂單後台 - 計算訂單列表需要抽成的金額，
	public void calculateListsAllocTotal() {

		List<ProOrderVO> CalculateListsAllocTotal = getAll();

		for (ProOrderVO saveAllocTotal : CalculateListsAllocTotal) {

			// 判斷是否有需要更新資料
			boolean update = false;

			if (saveAllocTotal.getProOrdAllocTotal() == null) {
				// 依照訂單的商品總金額（不含運不含折扣），計算平台抽成的金額。
				Integer finalAllocTotal = (int) (saveAllocTotal.getProTotal() * ALLOC_PER);
				saveAllocTotal.setProOrdAllocTotal(finalAllocTotal);

				update = true;
			}

			if (saveAllocTotal.getProOrdAllocSendFmem() == null) {
				// 計算平台撥款金額
				Integer proOrdAllocSendFmem = saveAllocTotal.getProTotal() - saveAllocTotal.getProOrdAllocTotal();
				saveAllocTotal.setProOrdAllocSendFmem(proOrdAllocSendFmem);

				update = true;
			}

			// 如果有更新資料，才做更新。
			if (update) {
				updateProOrder(saveAllocTotal);
			}
		}

	}
}