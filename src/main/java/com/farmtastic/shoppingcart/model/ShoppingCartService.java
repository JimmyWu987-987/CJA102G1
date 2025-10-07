package com.farmtastic.shoppingcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
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
    
    
    // 購物車的核心資料結構：直接儲存 ShoppingCartVO
    // 注意：這裡假設 memId 是從登入後的 Session 中取得，所以不儲存在 Service 層
    private List<ShoppingCartVO> cartItems = new ArrayList<>();
    
    // 尋找購物車中是否有某個商品
    private Optional<ShoppingCartVO> findItemByProId(Integer proId) {
        return cartItems.stream()
                .filter(item -> item.getProId().equals(proId))
                .findFirst();
    }
    
    // 計算單一 VO 的小計並設定
    private void calculateSubtotal(ShoppingCartVO shoppingCartVO) {
        // 使用 Integer 進行計算時要小心溢位，這裡假設單價和數量不會太大。
        Integer subTotal = shoppingCartVO.getCartUnitPrice() * shoppingCartVO.getCartAmount();
        shoppingCartVO.setCartSubTotal(subTotal);
    }
    
    // --- 核心功能 (CRUD) ---

    /**
     * C (Create/Add): 加入商品到購物車
     * @param product 要加入的商品資訊 (從資料庫或商品清單取得)
     * @param quantity 欲購買的數量
     * @param memId 使用者 ID (用於記錄在 VO 中，雖然主要用 Session 管理)
     */
    public void addProduct(Product product, Integer quantity) {
        if (quantity == null || quantity <= 0) return;

        Optional<ShoppingCartVO> existingItem = findItemByProId(product.getProId());

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
            newItem.setCartName(product.getProName());
            newItem.setCartUnitPrice(product.getProPrice());
            newItem.setCartAmount(quantity);
            calculateSubtotal(newItem);     
            
            cartItems.add(newItem);
        }
    }

    // *** 新增：當訪客登入後，用來更新購物車中所有 VO 的 memId ***
    /**
     * 在使用者登入後，將購物車中的所有項目綁定到新的會員 ID。
     * @param loggedInMemId 已登入的會員 ID
     */
    public void updateMemIdInCart(Integer loggedInMemId) {
        if (loggedInMemId != null && loggedInMemId > 0) {
            for (ShoppingCartVO item : this.cartItems) {
                item.setMemId(loggedInMemId);
            }
        }
    }

    /**
     * R (Read): 取得整個購物車的內容
     */
    public List<ShoppingCartVO> getCartItems() {
        // 返回一個新的 List，防止外部直接修改內部狀態
        return new ArrayList<>(this.cartItems);
    }

    /**
     * U (Update): 僅更新商品的數量
     * @param proId 要更新的商品 ID
     * @param newQuantity 新的購買數量 (使用者只能修改這個)
     * @return boolean, true 表示更新成功，false 表示商品不存在或數量無效
     */
    public boolean updateQuantity(Integer proId, Integer newQuantity) {
        if (newQuantity == null || newQuantity < 0) return false; 

        if (newQuantity.equals(0)) {
             // 如果數量設為 0，則直接移除商品
            return removeProduct(proId);
        }

        Optional<ShoppingCartVO> existingItem = findItemByProId(proId);

        if (existingItem.isPresent()) {
            ShoppingCartVO item = existingItem.get();
            item.setCartAmount(newQuantity);
            calculateSubtotal(item); // 重新計算小計
            return true;
        }
        return false;
    }

    /**
     * D (Delete/Remove): 移除商品
     * @param proId 要移除的商品 ID
     */
    public boolean removeProduct(Integer proId) {
        return cartItems.removeIf(item -> item.getProId().equals(proId));
    }

    /**
     * 清空購物車
     */
    public void clearCart() {
        // 清空 Session 中 cartItems 列表的所有內容
        this.cartItems.clear();
    }
    
    /**
     * 模擬結帳流程
     * 在實際應用中，這裡會執行以下操作：
     * 1. 檢查商品庫存 (如果還沒檢查)
     * 2. 建立訂單 (Order)
     * 3. 扣除庫存 (Product Stock)
     * 4. 清空購物車
     * * @return boolean 結帳是否成功
     */
    
    public ProOrderVO checkout(Integer memId,Mem loggedInMember ,double PER) {
        if (this.cartItems.isEmpty()) {
            return null; // 購物車是空的，無法結帳
        }

        // 這裡僅進行高階的模擬
        System.out.println("--- 模擬結帳開始 ---");
        System.out.println("訂單包含 " + this.cartItems.size() + " 個項目，總金額為: XXX"); 
        
        // 實際的結帳/扣庫存/寫入訂單邏輯...
        // 建立商品訂單
     			ProOrderVO proOrderVO = new ProOrderVO();

     			// 將session的值儲存至 proOrderVO.memVO.memId
     			Mem memVO = new Mem();
     			// 設定 memVO 的 memId
     			// 將包含 memId 的 memVO 設定給 proOrderVO
     			memVO.setMemId(memId);
     			proOrderVO.setMemVO(memVO);

     			// 查詢該會員"未使用"的"全部"商品折價卷明細
     			// 儲存 商品折價卷明細 的 商品折價卷編號

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
     			List<ShoppingCartVO> cartItems = getCartItems();
     			// 步驟 2: 遍歷購物車清單 (cartItems)
     			for(ShoppingCartVO shoppingCartVO : cartItems) {
     				// 步驟 3: 在迴圈內，建立一個新的 ProOrderItemVO 物件
     				ProOrderItemVO proOrderItemVO = new ProOrderItemVO();
     				Product productVO = new Product();
     				// 步驟 4: 取出 ShoppingCartVO 的欄位資料，存入 ProOrderItemVO
     				productVO.setProId(shoppingCartVO.getProId());
     				productVO.setProName(shoppingCartVO.getCartName());
     				
     				proOrderItemVO.setProductVO(productVO);
     				proOrderItemVO.setProUnitPrice(shoppingCartVO.getCartUnitPrice());
     				proOrderItemVO.setProAmount(shoppingCartVO.getCartAmount());
     				
     				Integer proSubTota = shoppingCartVO.getCartUnitPrice()*shoppingCartVO.getCartAmount();
     				proOrderItemVO.setProSubTota(proSubTota);
     				
     				// 步驟 5: 將新的 ProOrderItemVO 加入到訂單明細清單中
     				proOrderItemsList.add(proOrderItemVO);
     			}
     			
     			proOrderVO.setProOrderItems(proOrderItemsList);
     			

     			// 計算商品總金額
     			Integer proTotal = getCartTotal();
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
     			
     			// 折價券折抵金額
     			// 用memId查詢 同學寫好持有者明細
     			// 等同學寫好持有者明細
     			proOrderVO.setProOrdCpndisc(null);
     			Integer proOrdCpndisc = proOrderVO.getProOrdCpndisc(); // 先手動輸入
     			if(proOrdCpndisc == null) {
     				proOrdCpndisc = 0;
     			}
     			
     			// 會員持有點數
     			Integer memPoint = loggedInMember.getMemPoint();

     			// 商品訂單折抵會員點數
     			Integer proOrdPointdisc = proOrderVO.getProOrdPointdisc();
     			if(proOrdPointdisc == null) {
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
     			proOrderVO.setProOrdName(loggedInMember.getMemName());
     			
     			// 收件人電話
     			proOrderVO.setProOrdMobile(loggedInMember.getMemMobile());
     			
     			// 收件人電子郵件
     			proOrderVO.setProOrdEmail(loggedInMember.getMemEmail());
     			
     			// 收件人地址
     			String proOrdAddr = loggedInMember.getMemZipcode();
     			proOrdAddr += loggedInMember.getMemCity();
     			proOrdAddr += loggedInMember.getMemDist();
     			proOrdAddr += loggedInMember.getMemAddr();
     			proOrderVO.setProOrdAddr(proOrdAddr);

        // 結帳成功後，清空購物車
        clearCart(); 
        
        System.out.println("--- 模擬結帳成功，購物車已清空 ---");
        return proOrderVO;
    }

    /**
     * 計算購物車總金額
     */
    public Integer getCartTotal() {
        return cartItems.stream()
                .mapToInt(ShoppingCartVO::getCartSubTotal)
                .sum();
    }
}