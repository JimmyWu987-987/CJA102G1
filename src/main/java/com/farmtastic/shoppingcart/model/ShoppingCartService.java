package com.farmtastic.shoppingcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

// @SessionScope 確保這個 Service 實例綁定到單個使用者的 Session，
// 並自動將其狀態 (購物車內容) 存入 Redis。
@Service
@SessionScope
public class ShoppingCartService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 購物車的核心資料結構：直接儲存 ShoppingCartVO
    // 注意：這裡假設 memId 是從登入後的 Session 中取得，所以不儲存在 Service 層
    private List<ShoppingCartVO> cartItems = new ArrayList<>();

    // --- 輔助方法 ---

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
    public void addProduct(Product product, Integer quantity, Integer memId) {
        if (quantity == null || quantity <= 0) return;

        Optional<ShoppingCartVO> existingItem = findItemByProId(product.getProId());

        if (existingItem.isPresent()) {
            // 1. 如果商品已存在，則更新數量
            ShoppingCartVO item = existingItem.get();
            Integer newAmount = item.getCartAmount() + quantity;
            item.setCartAmount(newAmount);
            calculateSubtotal(item); // 重新計算小計
        } else {
            // 2. 如果商品不存在，則新增 ShoppingCartVO
            ShoppingCartVO newItem = new ShoppingCartVO();
            newItem.setMemId(memId);
            newItem.setProId(product.getProId());
            newItem.setCartName(product.getProName());
            newItem.setCartUnitPrice(product.getProPrice()); // 假設 proPrice 就是單價
            newItem.setCartAmount(quantity);
            calculateSubtotal(newItem); // 設定小計
            
            cartItems.add(newItem);
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
    
    public boolean checkout() {
        if (this.cartItems.isEmpty()) {
            return false; // 購物車是空的，無法結帳
        }

        // 這裡僅進行高階的模擬
        System.out.println("--- 模擬結帳開始 ---");
        System.out.println("訂單包含 " + this.cartItems.size() + " 個項目，總金額為: XXX"); 
        
        // 實際的結帳/扣庫存/寫入訂單邏輯...
        

        // 結帳成功後，清空購物車
        clearCart(); 
        
        System.out.println("--- 模擬結帳成功，購物車已清空 ---");
        return true;
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