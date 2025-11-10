// com.farmtastic.proorder.model.ProOrderSevice.java

package com.farmtastic.proorder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.memprocpn.model.MemProCpnRepository;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proorderitem.model.ProOrderItemId;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;
import com.farmtastic.shoppingcart.model.ShoppingCartService;

@Service
public class ProOrderService {

	@Autowired
	ProOrderRepository repository;
	@Autowired
	ProOrderItemService proOrdItemSvc;
	@Autowired
	MemProCpnRepository mpcRepository;
	@Autowired
	ProService productSvc;
	@Autowired
	MemService memSvc;
	@Autowired
	ShoppingCartService shoppingCartSvc;
	@Autowired
	MemProCpnServiceImp mpcSvc;

	// 每筆訂單的抽成百分筆
	private static final double ALLOC_PER = 0.1;

	// 新增
	@Transactional
	public void addProOrder(ProOrderVO proOrderVO) {
		// 將脫管的 Product 實體轉換為受管實體
		if (proOrderVO.getProOrderItems() != null) {
			for (ProOrderItemVO proOrderItemVO : proOrderVO.getProOrderItems()) {
				// 1. 商品訂單與訂單明細們的關聯
				proOrderItemVO.setProOrderVO(proOrderVO);

				// 目前 proOrderItemVO 內的 proVO 為游移狀態
				// 重新附加 (Re-attach) 游離的 Pro 實體
				// 用 proId 從資料庫查詢該 proVO，確保 proVO 為 JPA 託管
				Integer proId = proOrderItemVO.getProductVO().getProId();
				Pro managerProVO = productSvc.getOnePro(proId);
				proOrderItemVO.setProductVO(managerProVO);

				// 2. 將訂單明細的複合主鍵 ProOrderItemId 設定給 proId
				// 確保 proOrderItemId 非空值 (proOrderItemId 為一個物件)
				ProOrderItemId proOrderItemId = proOrderItemVO.getId();
				if (proOrderItemId == null) {
					proOrderItemId = new ProOrderItemId();
				}
				// 3. 從 ProVO 取得 proId, 設定給複合主鍵
				if (proOrderItemVO.getProductVO() != null) {
					proOrderItemId.setProId(proOrderItemVO.getProductVO().getProId());
				}
				// 4. proOrderItemId 已經有 proId 資訊，存回 item
				proOrderItemVO.setId(proOrderItemId);
			}
		}

		// 儲存訂單, 連帶儲存訂單明細
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
			proOrdItemSvc.deleteProOrderItem(proOrdId);
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

	// ====================================[一般會員前台]訂單====================================
	// 確定將訂單加入到DB的邏輯
	// @PostMapping("insert") 專用
	@Transactional
	public void finalCheckOrder(ProOrderVO proOrderVO) {
		if (proOrderVO.getProOrdCpndisc() == null) {
			proOrderVO.setProOrdCpndisc(0);
		}

		if (proOrderVO.getProOrdPointdisc() == null) {
			proOrderVO.setProOrdPointdisc(0);
		}
		if (proOrderVO.getProOrdPointGet() == null) {
			proOrderVO.setProOrdPointGet(0);
		}

		// 檢查是否有使用優惠券
		Integer cpnHolderDetailId = proOrderVO.getMemProCpnVO().getCpnHolderDetailId();
		boolean checkUseMcpn = checkUseMcpn(proOrderVO, cpnHolderDetailId);
		if (checkUseMcpn) {
			MemProCpnVO uesedMpc = mpcSvc.getOne(cpnHolderDetailId);
			proOrderVO.setMemProCpnVO(uesedMpc);
		} else {
			proOrderVO.setMemProCpnVO(null);
		}

		// 訂單狀態
		proOrderVO.setProOrdStatus((byte) 0);

		// 訂單付款狀態
		proOrderVO.setProPayStatus((byte) 0);

		// 平台撥款狀態，預設為0(未撥款)
		proOrderVO.setProOrdAllocStatus((byte) 0);

		// 平台抽成金額
		// 依照訂單的商品總金額（不含運不含折扣），計算平台抽成的金額。
		Integer ProOrdAllocTotal = (int) (proOrderVO.getProTotal() * ALLOC_PER);
		proOrderVO.setProOrdAllocTotal(ProOrdAllocTotal);

		// 平台撥款給小農的金額
		Integer proOrdAllocSendFmem = proOrderVO.getProTotal() - proOrderVO.getProOrdAllocTotal();
		proOrderVO.setProOrdAllocSendFmem(proOrdAllocSendFmem);

		// 假設總金額為0, 設定成未付款。
		// 前台會顯 0元購買
		if (proOrderVO.getProOrdGrandTotal() == 0) {
			// 0元購買
			proOrderVO.setProPayStatus((byte) 2);
			// 0元購買
			proOrderVO.setProOrdPayment((byte) 2);
		}

		// 設定關聯和明細
		Integer memId = proOrderVO.getMemVO().getMemId();
		Mem memVO = memSvc.getOneByMemId(memId);
		proOrderVO.setMemVO(memVO);

	}

	// 新增訂單有使用優惠卷，將優惠卷更改成已使用狀態。
	@Transactional
	public void mcpnUsed(ProOrderVO proOrderVO) {
		try {
			MemProCpnVO updateMpc = mpcSvc.getOne(proOrderVO.getMemProCpnVO().getCpnHolderDetailId());
			// 設定已經使用該折價券
			updateMpc.setCpnUseStatus(CpnUseStatus.USED);
			// 將最終點數結果，存回DB
			mpcSvc.updateMemProCpn(updateMpc);
		} catch (Exception e) {
			// 記錄錯誤但不影響訂單流程
			System.err.println("更新優惠券狀態失敗: " + e.getMessage());
		}
	}

	// 新增訂單後有點數回饋，將資料儲存至MemVO表單之會員點數欄位。
	public void updateMemPoint(ProOrderVO proOrderVO, Mem loggedInMember) {

		// 從proOrderVO取得此訂單的回饋點數，儲存至mem物件的會員點數欄位
		Integer memPoint = proOrderVO.getMemVO().getMemPoint();
		Integer memPointDisc = proOrderVO.getProOrdPointdisc();
		Integer memPointGet = proOrderVO.getProOrdPointGet();
		Integer finalMemPoint = memPoint - memPointDisc + memPointGet;
		loggedInMember.setMemPoint(finalMemPoint);

		// 將最終點數結果，存回DB
		memSvc.updateMem(loggedInMember);

	}

	// 新增訂單的扣商品庫存的邏輯
	@Transactional
	public Pro discProductStock(ProOrderVO proOrderVO) {
		List<ProOrderItemVO> finalItems = proOrderVO.getProOrderItems();
		List<Pro> discProList = new ArrayList<Pro>();

		for (ProOrderItemVO itemList : finalItems) {
			// 查詢該產品的庫存
			Pro tempProVO = productSvc.getOnePro(itemList.getProductVO().getProId());
			Integer originalStock = tempProVO.getProStock();
			Integer discStock = itemList.getProAmount();
			Integer finalStock = originalStock - discStock;

			if (finalStock < 0) {
				return tempProVO;
			} else {
				tempProVO.setProStock(finalStock);
				discProList.add(tempProVO);
			}
		}

		// 確認該訂單明細都沒有庫存的問題，才存入資料庫
		if (discProList != null || !discProList.isEmpty()) {
			for (Pro finalProVO : discProList) {
				productSvc.updatePro(finalProVO);
			}
		}

		// 返回空值，代表不用給controller抓取無法扣庫存的商品。
		// 代表成功扣除庫存
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
	public boolean checkUseMcpn(ProOrderVO proOrderVO, Integer cpnHolderDetailId) {

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

	// 取消訂單判斷是否要返還會員點數的邏輯
	// 業務邏輯是，有付款才會新增點數到 Mem 的 DB
	// false（未付款：不返還）
	// true（已付款：返還）
	@Transactional
	public void cancelOrderAndBackPoint(ProOrderVO proOrderVO) {

		switch (proOrderVO.getProPayStatus()) {
		case 0: // 該訂單是“未付款”，不需要返還點數
			break;
		case 1: // 該訂單是“已付款”，返還點數
			Integer memId = proOrderVO.getMemVO().getMemId();
			Mem memVO = memSvc.getOneByMemId(memId);

			Integer originalMemPoiont = memVO.getMemPoint();
			Integer backMemPoint = proOrderVO.getProOrdPointGet();
			Integer finalMemPoint = originalMemPoiont - backMemPoint;

			if (originalMemPoiont == 0 || finalMemPoint < 0) {
				// 如果會員點數已經是“0”，則維持“0點”會員點數。不做修改
				break;
			} else if (finalMemPoint > 0) {
				// 如果會員點數 > “0”，則返還點數，儲存至DB
				memVO.setMemPoint(finalMemPoint);
				memSvc.updateMem(memVO);
			}

			break;

		default:
			System.err.println("訂單編號[ " + proOrderVO.getProOrdId() + " ]的訂單狀態錯誤，沒有取消訂單，請洽系統管理員！");
			break;
		}

	}

	// 修改訂單的狀態
	// 最後返回的是文字訊息
	@Transactional
	public String updateProOrderStatus(ProOrderVO proOrderVO) {

		String successMessage = null;

		// 判斷是否要更新狀態
		boolean updateStatus = false;

		switch (proOrderVO.getProOrdStatus()) {
		// 訂單未出貨，可以直接取消訂單。
		case 0:
		case 1:
			System.out.println("訂單取消！");
			proOrderVO.setProOrdStatus((byte) 1);

			// 取消訂單返回庫存的邏輯
			cancelOrderAndBackStock(proOrderVO);

			// 取消訂單判斷是否要返還點數的邏輯
			// 業務邏輯是，有付款才會新增點數到 Mem 的 DB
			cancelOrderAndBackPoint(proOrderVO);

			successMessage = "訂單已經取消！";

			updateStatus = true;
			break;
		// 出貨中，通知賣家到貨
		case 2:

			System.out.println("已通知賣家到貨！");
			proOrderVO.setProOrdStatus((byte) 3);
			updateStatus = true;
			successMessage = "已通知賣家到貨！";
			break;

		case 3:
			// 訂單已經是退貨流程，直接返回。
		case 4:
			System.out.println("已通知賣家退貨！");
			proOrderVO.setProOrdStatus((byte) 5);
			updateStatus = true;
			successMessage = "已通知賣家退貨！";
			break;
		// 已經是退貨狀態，不會更新狀態
		// 已在前端隱藏退貨按鈕，以下判斷為預防用。
		case 5:
		case 6:
			System.out.println("已經是退貨狀態！");
			successMessage = "已經是退貨狀態！";
			break;
		default:
			successMessage = "更新訂單狀態異常，請洽網站管理員！";
			break;
		}

		if (updateStatus) {
			updateProOrder(proOrderVO);
		}

		return successMessage;
	}

	// 清除來自購物車的該訂單內容
	public void insertOrderCleanCart(ProOrderVO proOrderVO) {
		// 因為確定這份訂單內的產品，都是來自同一個小農fmemId
		// 所以直接找集合內的第一個物件，取出fmemId
		Integer fmemId = proOrderVO.getProOrderItems().get(0).getProductVO().getFmemId().getFmemId();
		shoppingCartSvc.clearCartByFmemId(fmemId);
	}

	// ====================================[後台]使用金流系統使用====================================

	// 查詢該小農“已付款“之“已到貨”以及“已退貨的”全部訂單，可以撥款的訂單
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