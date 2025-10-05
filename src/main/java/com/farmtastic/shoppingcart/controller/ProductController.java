package com.farmtastic.shoppingcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.shoppingcart.model.Product; // <--- 修正：套件路徑應為 product.model
import com.farmtastic.shoppingcart.model.ProductService; // <--- 新增導入

@Controller
@RequestMapping("/cart/products")
public class ProductController {
    
    private final ProductService productService; // <--- 新增 ProductService 欄位

    @Autowired
    public ProductController(ProductService productService) { // <--- 建構子注入
        this.productService = productService;
    }

    // **************************** 顯示商品清單 ****************************

    // URL: GET /cart/products/list
    @GetMapping("/list")
    public String listProducts(Model model) {
        // 透過 ProductService 取得商品清單
        List<Product> products = productService.getAllProducts();
        
        model.addAttribute("products", products);
        
        // 返回 Thymeleaf 模板名稱
        return "front_end/customer/unlogined/shoppingCart/productList"; 
    }
}