package com.farmtastic.shoppingcart.model; // 建議放在此處

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    
    // **************************** 輔助方法：模擬資料庫查詢 ****************************
    // 在真實專案中，這裡會注入 ProductRepository 來執行 findById 或 findAll

    /**
     * 從資料庫中模擬取得單一商品
     */
    public Product getProductById(Integer proId) {
        // 這是從 ShoppingCartController 移過來的邏輯
        if (proId.equals(101)) {
            Product p = new Product();
            p.setProId(101);
            p.setProName("有機蘋果");
            p.setProPrice(50); 
            p.setProStock(100); 
            return p;
        } else if (proId.equals(102)) {
            Product p = new Product();
            p.setProId(102);
            p.setProName("新鮮雞蛋");
            p.setProPrice(12); 
            p.setProStock(200);
            return p;
        }
        return null;
    }

    /**
     * 從資料庫中模擬取得所有商品 (供 ProductController 呼叫)
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        
        // 呼叫 getProductById 模擬取得清單
        Product p1 = getProductById(101);
        if (p1 != null) products.add(p1);
        
        Product p2 = getProductById(102);
        if (p2 != null) products.add(p2);
        
        return products;
    }
}