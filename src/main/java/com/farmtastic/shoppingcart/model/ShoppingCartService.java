package com.farmtastic.shoppingcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

// @SessionScope 確保這個 Service 實例綁定到單個使用者的 Session，
// 並自動將其狀態 (購物車內容) 存入 Redis。
@Service
//@SessionScope
public class ShoppingCartService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	FmemService fmemSvc;
	@Autowired
	MemService memSvc;
	@Autowired
	ProductService productSvc;

	// 🌟 核心修改：使用 Map<FmemId, List<ShoppingCartVO>> 儲存購物車 🌟
	// Key: 小農 ID (fmemId)，Value: 該小農底下的商品清單
	private Map<Integer, List<ShoppingCartVO>> groupedCartItems = new HashMap<>();

	// 購物車的核心資料結構：直接儲存 ShoppingCartVO
	// 注意：這裡假設 memId 是從登入後的 Session 中取得，所以不儲存在 Service 層
//    private List<ShoppingCartVO> cartItems = new ArrayList<>();

	// 取得特定小農的購物車清單
	private List<ShoppingCartVO> getCartItemsByFmemId(Integer fmemId) {
		return groupedCartItems.computeIfAbsent(fmemId, k -> new ArrayList<>());
	}

	// 尋找特定小農購物車中是否有某個商品
	private Optional<ShoppingCartVO> findItemByProId(Integer fmemId, Integer proId) {
		List<ShoppingCartVO> items = getCartItemsByFmemId(fmemId);
		return items.stream().filter(item -> item.getProId().equals(proId)).findFirst();
	}

	// 計算單一 VO 的小計並設定
	private void calculateSubtotal(ShoppingCartVO shoppingCartVO) {
		Integer subTotal = shoppingCartVO.getCartUnitPrice() * shoppingCartVO.getCartAmount();
		shoppingCartVO.setCartSubTotal(subTotal);
	}

	// --- 核心功能 (CRUD) ---

	/**
	 * C (Create/Add): 加入商品到購物車
	 * 
	 * @param product  要加入的商品資訊 (已包含 fmemId)
	 * @param quantity 欲購買的數量
	 */
	public void addProduct(Product product, Integer quantity) {
		if (quantity == null || quantity <= 0)
			return;

		// 取得小農ID
		Integer fmemId = product.getFmemVO().getFmemId();
		// 假設該商品沒有小農編號
		if (fmemId == null) {
			System.err.println("商品編號:[ " + product.getProId() + " ]，沒有所屬小農編號，請確認該商品所屬小農編號。");
			return;
		}
		// 取得或創建該小農的專屬購物車
		List<ShoppingCartVO> cart = getCartItemsByFmemId(fmemId);
		Optional<ShoppingCartVO> existingItem = findItemByProId(fmemId, product.getProId());

		if (existingItem.isPresent()) {
			// 1. 如果商品已存在，則更新數量
			ShoppingCartVO item = existingItem.get();
			Integer newAmount = item.getCartAmount() + quantity;
			item.setCartAmount(newAmount);
			calculateSubtotal(item); // 重新計算小計
		} else {
			// 如果購物車中沒有此商品，則新增項目
			ShoppingCartVO newItem = new ShoppingCartVO();
			// *** memId 暫時為 null 或 0，表示訪客購物 ***
			newItem.setMemId(null);
			newItem.setProId(product.getProId());
			newItem.setFmemId(fmemId);
			newItem.setCartName(product.getProName());
			newItem.setCartUnitPrice(product.getProPrice());
			newItem.setCartAmount(quantity);
			calculateSubtotal(newItem);

			cart.add(newItem);
		}
	}

	// *** 新增：當訪客登入後，用來更新購物車中所有 VO 的 memId (針對所有項目) ***
	// (此方法邏輯不變，但現在需要遍歷 Map 中的所有 List)
	/**
	 * 在使用者登入後，將購物車中的所有項目綁定到新的會員 ID。
	 * 
	 * @param loggedInMemId 已登入的會員 ID
	 */
	public void updateMemIdInCart(Integer loggedInMemId) {
		if (loggedInMemId != null && loggedInMemId > 0) {
			for (List<ShoppingCartVO> mapItems : this.groupedCartItems.values()) {
				for (ShoppingCartVO item : mapItems) {
					item.setMemId(loggedInMemId);
				}
			}
		}
	}

	/**
	 * R (Read): 取得整個分組後的購物車內容
	 * 
	 * @return Map<Integer, List<ShoppingCartVO>> key為 fmemId
	 */
	public Map<Integer, List<ShoppingCartVO>> getGroupedCartItems() {
		// 返回一個新的 Map，防止外部直接修改內部狀態
		return new HashMap<>(this.groupedCartItems);
	}

	// *** R (Read): 取得所有購物車項目的扁平化清單 (如果你仍需要全部清單) ***
	public List<ShoppingCartVO> getAllCartItems() {
		List<ShoppingCartVO> allItems = new ArrayList<>();
		for (List<ShoppingCartVO> items : this.groupedCartItems.values()) {
			allItems.addAll(items);
		}
		return allItems;
	}

	/**
	 * U (Update): 僅更新商品的數量 (現在需要 fmemId 來定位)
	 * 
	 * @param proId       要更新的商品 ID
	 * @param fmemId      商品所屬的小農 ID 🌟 新增參數 🌟
	 * @param newQuantity 新的購買數量
	 */
	public boolean updateQuantity(Integer proId, Integer fmemId, Integer newQuantity) {
		// 檢查數量是否為正數
		if (newQuantity == null || newQuantity < 0)
			return false;

		// 檢查該小農購物車是否有物件
		List<ShoppingCartVO> cart = getCartItemsByFmemId(fmemId);
		if (cart == null) {
			return false;
		}

		Optional<ShoppingCartVO> existingItem = findItemByProId(fmemId, proId);

		if (existingItem.isPresent()) {

			// 如果數量設為 0，則直接移除商品
			if (newQuantity.equals(0)) {
				return removeProduct(proId, fmemId);
			}

			ShoppingCartVO item = existingItem.get();
			item.setCartAmount(newQuantity);
			calculateSubtotal(item); // 重新計算小計

			// 檢查該小農購物車是否清空，如果清空則移除 Map 中的 Key
			if (cart.isEmpty()) {
				groupedCartItems.remove(fmemId);
			}
			return true;
		}
		return false;
	}

	/**
	 * D (Delete/Remove): 移除商品 (現在需要 fmemId 來定位)
	 * 
	 * @param proId  要移除的商品 ID
	 * @param fmemId 商品所屬的小農 ID 🌟 新增參數 🌟
	 */

	public boolean removeProduct(Integer proId, Integer fmemId) {
		List<ShoppingCartVO> cart = groupedCartItems.get(fmemId);

		if (cart == null) {
			return false;
		}

		boolean removed = cart.removeIf(item -> item.getProId().equals(proId));

		return removed;
	}
	
	/**
	 * 🌟 新增功能：清空指定小農 ID 的購物車 (清空 Map 中一個 Key 的 Value) 🌟
	 * @param fmemId 要清空的指定小農 ID
	 */
	public void clearCartByFmemId(Integer fmemId) {
		if(fmemId != null || groupedCartItems.containsKey(fmemId)) {
			groupedCartItems.remove(fmemId);
			System.out.println("--- 成功清空小農 ID: " + fmemId + " 的購物車 ---");
		} else {
			System.out.println("--- 警告：找不到小農 ID: " + fmemId + " 的購物車，無法清空 ---");
		}
	}

	/**
	 * 清空購物車 (清空整個 Map)
	 */
	public void clearAllCarts() {
		this.groupedCartItems.clear();
	}

	/**
	 * 模擬結帳流程 (針對特定小農的購物車) 
     * @param fmemId 要結帳的小農 ID
     * @return ProOrderVO 
     * 將購物車物件資訊，移存到訂單物件（包含訂單明細） 
     * 訂單物件（包含訂單明細）暫存在 session，參考 ShoppingCartController.java
	 * 1. 檢查商品庫存 (如果還沒檢查) 
	 * 2. 建立訂單 (Order) 
	 * 3. 扣除庫存 (Product Stock) 
	 * 4. 清空購物車 
	 * * @return boolean 結帳是否成功
	 */

	public ProOrderVO checkoutByFmemId(Integer fmemId,Integer memId, Mem loggedInMember, double PER) {
		List<ShoppingCartVO> cartItemsForFmem = groupedCartItems.get(fmemId);
		
		if (cartItemsForFmem == null || cartItemsForFmem.isEmpty()) {
			return null; // 該小農購物車是空的，無法結帳
		}

		// 這裡僅進行高階的模擬
		System.out.println("--- 模擬結帳開始 ---");
		System.out.println("訂單包含 " + cartItemsForFmem.size() + " 個項目，總金額為: XXX");

		// 實際的結帳/扣庫存/寫入訂單邏輯...
		// 建立商品訂單
		ProOrderVO proOrderVO = new ProOrderVO();

		// 將session的值儲存至 proOrderVO.memVO.memId
		Mem memVO = memSvc.getOneByMemId(memId);
		// 設定 memVO 的 memId
		// 將包含 memId 的 memVO 設定給 proOrderVO
		proOrderVO.setMemVO(memVO);

		// 新增訂單日期為當下系統時間
		// 讀取毫秒
		// 將日期格式轉成 yyyy-MM-dd HH:mm:ss，由JPA處理日期格式(ProOrderVO第47行)
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		// 存入proOrderVO物件
		proOrderVO.setProOrdDate(currentTimestamp);

		// 訂單狀態預設為(0:成立訂單)
		proOrderVO.setProOrdStatus((byte) 0);
		// 付款狀態預設為(0:未付款)
		proOrderVO.setProPayStatus((byte) 0);

		// 顯示從購物車傳入的商品訂單明細
		// 將購物車的session 存入商品訂單明細
		// 步驟 1: 宣告一個新的 List 來存放轉換後的商品訂單明細
		List<ProOrderItemVO> proOrderItemsList = new ArrayList<>();
//		List<ShoppingCartVO> cartItems = getCartItems();
		// 步驟 2: 遍歷購物車清單 (cartItems)
		for (ShoppingCartVO shoppingCartVO : cartItemsForFmem) {
			// 步驟 3: 在迴圈內，建立一個新的 ProOrderItemVO 物件
			ProOrderItemVO proOrderItemVO = new ProOrderItemVO();
			Product productVO = new Product();
			// 步驟 4: 取出 ShoppingCartVO 的欄位資料，存入 ProOrderItemVO
			productVO.setProId(shoppingCartVO.getProId());
			productVO.setProName(shoppingCartVO.getCartName());

			proOrderItemVO.setProductVO(productVO);
			proOrderItemVO.setProUnitPrice(shoppingCartVO.getCartUnitPrice());
			proOrderItemVO.setProAmount(shoppingCartVO.getCartAmount());
			proOrderItemVO.setProSubTotal(shoppingCartVO.getCartSubTotal());

			// 步驟 5: 將新的 ProOrderItemVO 加入到訂單明細清單中
			proOrderItemsList.add(proOrderItemVO);
			
		}

		proOrderVO.setProOrderItems(proOrderItemsList);

		// 計算商品總金額
		Integer proTotal = getCartTotalByFmemId(fmemId); 
		proOrderVO.setProTotal(proTotal);

		// 計算運費金額
		// 這邊要寫一個fmem的service的方法
		Optional<Fmem> fmemlist = fmemSvc.getOneByFmemId(memId);
		// 如果 Optional 包含 Fmem，則取出它；否則，建立並使用一個新的 Fmem() 物件作為預設值。
		Fmem fmem = fmemlist.orElse(new Fmem());
		// 查詢小農的運費
		Integer prodFee = fmem.getProdFee();
		// 判斷運費欄位是否為null
		if (prodFee == null) {
			prodFee = 0; // 如果小農沒設定運費，則預設為0
		}
		proOrderVO.setProOrdShipFee(prodFee);

		// 查詢該會員"未使用"的"全部"商品折價卷明細
		// 儲存 商品折價卷明細 的 商品折價卷編號
		// 這邊先預設為null
		proOrderVO.setMemProCpnVO(null);

		// 折價券折抵金額
		// 用memId查詢 同學寫好持有者明細
		// 等同學寫好持有者明細
		Integer proOrdCpndisc = proOrderVO.getProOrdCpndisc(); // 先手動輸入
		if (proOrdCpndisc == null) {
			proOrdCpndisc = 0;
			proOrderVO.setProOrdCpndisc(proOrdCpndisc);
		}

		// 會員持有點數

		Integer memPoint = memVO.getMemPoint();

		// 商品訂單折抵會員點數
		Integer proOrdPointdisc = proOrderVO.getProOrdPointdisc();
		if (proOrdPointdisc == null) {
			proOrdPointdisc = 0;
		}
		proOrderVO.setProOrdPointdisc(proOrdPointdisc);
		// 修改該會員點數
		// 這邊要寫一個修改mem的service
		memPoint = memPoint - proOrdPointdisc;
		memVO.setMemPoint(memPoint);

		// 實付金額
		// 實付金額 = 商品總金額 + 運費 - 折價券折抵金額 - 訂單折抵會員點數
		Integer proOrdGrandTotal = proTotal + prodFee - proOrdCpndisc - proOrdPointdisc;
		proOrderVO.setProOrdGrandTotal(proOrdGrandTotal);

		// 訂單回饋會員點數
		// 回饋 1%
		// 無條件捨去小數點
		Integer proOrdPointGet = (int) (Math.floor(proTotal * PER));
		proOrderVO.setProOrdPointGet(proOrdPointGet);

		// 物流追蹤碼
		// 小農前台做修改
		// 預設可以null

		// 出貨日期
		// 小農前台做修改
		// 預設可以null

		// 收件人姓名
		proOrderVO.setProOrdName(memVO.getMemName());

		// 收件人電話
		proOrderVO.setProOrdMobile(memVO.getMemMobile());

		// 收件人電子郵件
		proOrderVO.setProOrdEmail(memVO.getMemEmail());

		// 收件人地址
		String proOrdAddr = memVO.getMemZipcode();
		proOrdAddr += memVO.getMemCity();
		proOrdAddr += memVO.getMemDist();
		proOrdAddr += memVO.getMemAddr();
		proOrderVO.setProOrdAddr(proOrdAddr);

		// 結帳成功後，清空該小農的購物車
		groupedCartItems.remove(fmemId);

		System.out.println("--- 模擬結帳成功，購物車已清空 ---");
		return proOrderVO;
	}

	/**
	 * 計算特定小農 (fmemId) 購物車的總金額
	 * * @param fmemId 要計算總金額的小農 ID
	 * @return Integer 該小農購物車商品的總小計
	 */
	
	public Integer getCartTotalByFmemId(Integer fmemId) {
		// 1. 根據 fmemId 取得該小農的購物車清單
		List<ShoppingCartVO> cartItemsForFmem = groupedCartItems.get(fmemId);

		// 2. 檢查清單是否存在且不為空
		if (cartItemsForFmem == null || cartItemsForFmem.isEmpty()) {
			return 0; // 如果清單為空或不存在，總金額為 0
		}

		// 3. 使用 Stream API 計算所有項目的小計總和
		return cartItemsForFmem.stream()
				.mapToInt(ShoppingCartVO::getCartSubTotal) // 將每個 ShoppingCartVO 映射到它的 cartSubTotal (int)
				.sum(); // 計算所有小計的總和
	}
}