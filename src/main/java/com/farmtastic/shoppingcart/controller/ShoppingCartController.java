package com.farmtastic.shoppingcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.member.model.Mem;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.shoppingcart.model.Product;
import com.farmtastic.shoppingcart.model.ProductService;
import com.farmtastic.shoppingcart.model.ShoppingCartService;
import com.farmtastic.shoppingcart.model.ShoppingCartVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController { // 類別名稱修正為標準的 Controller
	
	// Points Earning Rate
	// 計算消費商品的總金額(金額不含運費)
	private final static double PER = 0.01;
	
	// 注入 @SessionScope 的購物車服務
	// 使用 final 確保 Service 不變，並透過建構子注入，是 Spring 推薦的做法
	private final ShoppingCartService cartService;
	private final ProductService productService; // <--- 新增 ProductService 欄位

	@Autowired
	public ShoppingCartController(ShoppingCartService cartService, ProductService productService) { // <--- 修正建構子
		this.cartService = cartService;
		this.productService = productService; // <--- 初始化 ProductService
	}

	// **************************** 1. 顯示購物車 (Read) ****************************

	// URL: GET /cart/view
	@GetMapping("/view")
	public String viewCart(Model model) {
		// 取得購物車清單和總金額，傳遞給 Thymeleaf 頁面
		List<ShoppingCartVO> items = cartService.getCartItems();
		model.addAttribute("cartItems", items);
		model.addAttribute("cartTotal", cartService.getCartTotal());

		// 返回 Thymeleaf 模板名稱 (對應 /src/main/resources/templates/cart/cartView.html)
		return "front_end/customer/unlogined/shoppingCart/cartView";
	}

	// ---

	// **************************** 2. 加入商品 (Create) ****************************

	// URL: POST /cart/add
	@PostMapping("/add")
	public String addProductToCart(@RequestParam("proId") Integer proId,
			@RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
			// RedirectAttributes 用於在重定向後傳遞一次性的成功/錯誤訊息
			RedirectAttributes redirectAttributes) {

		Product product = productService.getProductById(proId);

		if (product != null && quantity > 0) {
			// *** 修正：不再傳遞 memId，由 Service 內部處理 memId = 0 (訪客) ***
			cartService.addProduct(product, quantity);
			redirectAttributes.addFlashAttribute("successMessage", product.getProName() + " 成功加入購物車！");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "加入購物車失敗，商品不存在或數量無效。");
		}

		// 使用重定向 (redirect) 到顯示頁面，遵循 Post/Redirect/Get 模式
		return "redirect:/cart/products/list";
	}

	// ---

	// **************************** 3. 更新數量 (Update) ****************************

	// URL: POST /cart/update
	@PostMapping("/update")
	public String updateCartQuantity(@RequestParam("proId") Integer proId, @RequestParam("quantity") Integer quantity, // 這是使用者唯一能修改的欄位
			RedirectAttributes redirectAttributes) {

		boolean success = cartService.updateQuantity(proId, quantity);

		if (success) {
			redirectAttributes.addFlashAttribute("successMessage", "商品數量已更新！");
		} else if (quantity != null && quantity.equals(0)) {
			// 數量為 0 在 Service 中會被視為移除，這裡可以給出移除成功的訊息
			redirectAttributes.addFlashAttribute("successMessage", "商品已從購物車中移除。");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "更新數量失敗，請檢查商品 ID 或數量。");
		}

		return "redirect:/cart/view";
	}

	// ---

	// **************************** 4. 移除商品 (Delete) ****************************

	// URL: POST /cart/remove
	@PostMapping("/remove")
	public String removeProductFromCart(@RequestParam("proId") Integer proId, RedirectAttributes redirectAttributes) {

		boolean removed = cartService.removeProduct(proId);

		if (removed) {
			redirectAttributes.addFlashAttribute("successMessage", "商品已成功移除。");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "移除失敗，購物車中找不到該商品。");
		}

		return "redirect:/cart/view";
	}
	
	// **************************** 5. 清空購物車 ****************************

		// URL: POST /cart/clear
		@PostMapping("/clear")
		public String clearCart(RedirectAttributes redirectAttributes) {

			cartService.clearCart();

			redirectAttributes.addFlashAttribute("successMessage", "購物車已清空！");

			return "redirect:/cart/view";
		}

		// **************************** 6. 結帳 (Checkout) ****************************

		// URL: POST /cart/checkout
		@PostMapping("/checkout")
		public String checkout(RedirectAttributes redirectAttributes,HttpSession session,Model model) {

			// *** 登入檢查邏輯 ***
			// 在真實專案中，這裡會檢查 Spring Security 的 Context 或 Session 中是否有使用者物件
			// 取得 session 的會員資訊
			Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
			Integer memId = (Integer) session.getAttribute("memId");
			String memName = (String) session.getAttribute("memName");
			
			if (loggedInMember == null || memId == null || memName == null || memName.trim().isEmpty()){
				// 如果未登入，則導向登入頁面，並在 URL 中帶上「要結帳」的指示
				redirectAttributes.addFlashAttribute("errorMessage", "請先登入或註冊以完成結帳！");
				// *** 假設您的登入頁面是 /login ***
				return "redirect:/mem/showMemRegLoginForm";
			} else {
				
				// 這是處理使用者剛才登入的動作
				cartService.updateMemIdInCart(memId); // <--- 新增：更新購物車所有項目的 memId
				
			}
			
			ProOrderVO cartToProOrder = cartService.checkout(memId,loggedInMember,PER);

			if (cartToProOrder != null ) {
				redirectAttributes.addFlashAttribute("successMessage", "結帳成功！您的訂單已送出。");
			    // 修正後的程式碼行：使用 Flash Attribute 傳輸物件
			    redirectAttributes.addFlashAttribute("cartToProOrder", cartToProOrder);

				return "redirect:/mem/proorders/addProOrder";
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "結帳失敗！您的購物車是空的。");
				// 返回商品頁面
				return "/cart/products/list";
			}


			
		}
	}
	