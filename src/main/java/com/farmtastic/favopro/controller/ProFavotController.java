package com.farmtastic.favopro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.shoppingcart.model.Product;
import com.farmtastic.shoppingcart.model.ProductService;

//測試收藏之商品
@Controller
@RequestMapping("/favo/products")
public class ProFavotController {
	private final ProductService productService; // <--- 新增 ProductService 欄位

	@Autowired
	public ProFavotController(ProductService productService) { // <--- 建構子注入
		this.productService = productService;
	}

	// **************************** 顯示商品清單 ****************************

	// URL: GET /favo/products_test/list
	@GetMapping("/list")
	public String listProducts(Model model) {
		// 透過 ProductService 取得商品清單
		List<Product> products = productService.getAllProducts();

		model.addAttribute("products", products);

		// 返回 Thymeleaf 模板名稱
		return "front_end/customer/unlogined/favo_test/list";
	}
}
