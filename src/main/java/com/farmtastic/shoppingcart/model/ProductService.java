// com.farmtastic.shoppingcart.model.ProductService.java (修正版)

package com.farmtastic.shoppingcart.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    // 🌟 注入真正的 Repository 🌟
    @Autowired
    private ProductRepository productRepository; 

    /**
     * 從資料庫中取得單一商品
     */
    // 這裡不需要 @Transactional，因為 ProOrderSevice 已經有
    public Product getProductById(Integer proId) {
        Optional<Product> productOptional = productRepository.findById(proId);
        
        // 🚨 建議加上錯誤處理，如果商品 ID 不存在應拋出例外
        return productOptional.orElseThrow(
            () -> new RuntimeException("商品 ID: " + proId + " 不存在，無法建立訂單明細。")
        );
    }
    
    public List<Product> getAllProducts(){
    	return productRepository.findAll();
    }
    
    // ... (保留 getAllProducts 或其他方法，並用 Repository 實作)
}