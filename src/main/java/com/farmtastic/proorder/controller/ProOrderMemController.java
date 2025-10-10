package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemId;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;
import com.farmtastic.shoppingcart.model.ShoppingCartService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem/proorders")
public class ProOrderMemController {

	// Points Earning Rate
	// 計算消費商品的總金額(金額不含運費)
	private final static double PER = 0.01;

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService proOrderItemSvc;
	@Autowired
	FmemService femSvc;
	@Autowired
	MemService memSvc;
	@Autowired
	ShoppingCartService shoppingCartSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");

		// 錯誤驗證
		try {
			Mem MemVO = new Mem();
			MemVO.setMemId(memId);
			List<ProOrderVO> list = proOrdSvc.getAllByMemId(MemVO);

			// 將值回傳至前端thymeleaf
			model.addAttribute("proOrderList", list);
		} catch (Exception e) {
			// TODO: handle exception

		}
		return "/front_end/customer/logined/memProOrders/listAllProOrder";
	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		List<ProOrderItemVO> items = proOrderItemSvc.getProOrderItems(proOrderVO);

		// 將值回傳至前端thymeleaf
		model.addAttribute("proOrderVO", proOrderVO);
		model.addAttribute("proOrderItems", items);

		return "/front_end/customer/logined/memProOrders/listOneProOrder";
	}

	// 進入新增訂單頁面(view)
	@GetMapping("addProOrder")
	public String addProOrder(HttpSession session, ModelMap model) {

		// 取得 session 的會員資訊
		ProOrderVO cartToProOrder = (ProOrderVO) session.getAttribute("cartToProOrder");

		// 如果沒有 cartToProOrder (即非從結帳頁面重定向而來)，直接返回至購物車頁面。
		// 這樣可防止用戶直接訪問此 URL 時發生錯誤
		if (cartToProOrder == null) {
	        // 如果沒有暫存訂單，導回購物車頁面
	        return "redirect:/cart/view";
		} else {
			// 將值回傳至前端thymeleaf
			model.addAttribute("cartToProOrder", cartToProOrder);

			return "/front_end/customer/logined/memProOrders/addProOrder";

		}

	}

	// URL: POST /mem/proorders/insert
	/**
	 * 處理訂單最終提交的 POST 請求
	 */
	@PostMapping("insert")
	public String insert(@Valid ProOrderVO proOrderVO,
			BindingResult result,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			ModelMap model) {

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		ProOrderVO finalProOrderVO = (ProOrderVO) session.getAttribute("cartToProOrder");
		List<ProOrderItemVO> finalItems = finalProOrderVO.getProOrderItems();
		// 輸入資料的錯誤驗證
		if (result.hasErrors()) {
			// 取得所有錯誤的列表
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors) {
				// 這裡可以讀取錯誤代碼、錯誤訊息等
				System.out.println(error.getDefaultMessage());

				model.addAttribute("errorMessage", error.getDefaultMessage());
			}
			// 如果有錯誤，將原始的 cartToProOrder 和其他必要資料重新傳回頁面
			model.addAttribute("cartToProOrder", finalProOrderVO);
			return "forward:/mem/proorders/addProOrder?error";
		} else {
			// 將所有可能為 NULL 的金額屬性從 finalProOrderVO 複製過來 🌟
			proOrderVO.setProOrdDate(finalProOrderVO.getProOrdDate());
			proOrderVO.setProOrdCpndisc(
					finalProOrderVO.getProOrdCpndisc() != null ? finalProOrderVO.getProOrdCpndisc() : 0);
			proOrderVO.setProOrdGrandTotal(finalProOrderVO.getProOrdGrandTotal());
			proOrderVO.setProTotal(finalProOrderVO.getProTotal());
			proOrderVO.setProOrdShipFee(finalProOrderVO.getProOrdShipFee());
			proOrderVO.setProOrdPointdisc(
					finalProOrderVO.getProOrdPointdisc() != null ? finalProOrderVO.getProOrdPointdisc() : 0);
			proOrderVO.setProOrdPointGet(
					finalProOrderVO.getProOrdPointGet() != null ? finalProOrderVO.getProOrdPointGet() : 0);

//		    // 3. 設定關聯和明細
			proOrderVO.setMemVO(loggedInMember);
			proOrderVO.setProOrderItems(finalItems);
			
			// 🌟 關鍵修正 2：在 Controller/Service 確保明細回指主表 **並初始化複合主鍵 (ProId)** 🌟
			for (ProOrderItemVO item : finalItems) {
				// 1. 建立雙向關聯：讓每個明細知道它屬於哪個訂單 (proOrdId會在儲存時由JPA處理)
				item.setProOrderVO(proOrderVO);

				// 2. 🌟 關鍵修正：手動初始化 ProOrderItemId 並設定 proId 🌟
				// 由於 ProOrderItemVO 使用 @EmbeddedId 和 @MapsId 且 proId 是現有外鍵，
				// 我們必須確保 ProOrderItemId 實體本身已存在並包含 proId 值。

				// 確保 id 欄位已被初始化
				ProOrderItemId id = item.getId();
				if (id == null) {
					id = new ProOrderItemId();
				}

				// 從 Product 實體取得 proId，並設定給複合主鍵
				// 假設您的 Product 實體有 getProId() 方法
				id.setProId(item.getProductVO().getProId());

				// 將設定好 proId 的 ProOrderItemId 設回給 ProOrderItemVO
				item.setId(id);
				
			}

			try {
				// proOrdSvc 是 ProOrderSevice 的實例
				proOrdSvc.addProOrder(proOrderVO);

			} catch (RuntimeException e) {
				// 捕捉 Service 拋出的商品 ID 缺失或其他錯誤
				model.addAttribute("errorMessage", "新增訂單失敗：" + e.getMessage());
				model.addAttribute("memVO", loggedInMember);
				model.addAttribute("cartToProOrder", finalProOrderVO);
				return "/front_end/customer/logined/memProOrders/addProOrder";
			}
			// ================== 會員點數新增修改的邏輯 ======================
			// 從proOrderVO取得此訂單的回饋點數，儲存至mem物件的會員點數欄位
			Integer memPoint = proOrderVO.getMemVO().getMemPoint();
			Integer memPointDisc = proOrderVO.getProOrdPointdisc();
			Integer memPointGet = proOrderVO.getProOrdPointGet();
			Integer finalMemPoint = memPoint - memPointDisc + memPointGet;
			loggedInMember.setMemPoint(finalMemPoint);
			
			// 將最終點數結果，存回DB
			memSvc.updateMem(loggedInMember);
			
			// 更新網頁會員的session的資料
			session.setAttribute("loggedInMember", loggedInMember);
			// ================== 扣商品庫存的邏輯 ======================
			// 等同學寫好ORM
			// 未完成

			// 清除 Session 相關屬性
			session.removeAttribute("cartToProOrder");
			
			// 清除 該訂單的購物車內容
			// 因為確定這份訂單內的產品，都是來自同一個小農fmemId
			// 所以直接找集合內的第一個物件，取出fmemId
			Integer fmemId = proOrderVO.getProOrderItems()
					.get(0)
					.getProductVO()
					.getFmemVO()
					.getFmemId();
			shoppingCartSvc.clearCartByFmemId(fmemId);
			
			// 重導向到訂單列表頁面
			redirectAttributes.addFlashAttribute("successMessage", "新的訂單已成功建立！");
			return "redirect:/mem/proorders/listAllProOrder";

		}

	}

	// 修改訂單 (處理點數折抵)
	@PostMapping("OrdPointDiscUpdate")
	public String update(@RequestParam(name="proOrdPointdisc", required = false) String proOrdPointdisc, HttpSession session,ModelMap model,RedirectAttributes redirectAttributes) {

		// proOrdPointdisc 當輸入金額為0或空字串，直接返回
		if (proOrdPointdisc == null || proOrdPointdisc.trim().isEmpty()) {

			redirectAttributes.addFlashAttribute("successMessage", "點數折抵沒有更新！"); // 可選：顯示成功訊息
			return "redirect:addProOrder";
		} else {
			
	        Integer tempProOrdPointdisc = Integer.valueOf(proOrdPointdisc);
		

		// 1. 從 Session 取得原始的訂單資訊
		ProOrderVO finalProOrderVO = (ProOrderVO) session.getAttribute("cartToProOrder");
		Integer memId = (Integer) session.getAttribute("memId");
		
		if (finalProOrderVO == null) {
			model.addAttribute("errorMessage", "購物車資訊已遺失，請重新結帳！");
			return "redirect:/"; // 導回首頁或購物車頁面
		}

		// 2. 驗證點數折抵值 (防止惡意輸入或超過持有/總額)
		Mem memVO = memSvc.getOneByMemId(memId);
		Integer memPoint = memVO.getMemPoint();

		// (1) 確保折抵點數不超過會員持有總點數
		if (tempProOrdPointdisc > memPoint) {
			tempProOrdPointdisc = memPoint; // 限制最多只能折抵會員持有點數
		}

		// (2) 確保折抵點數轉換的金額不超過「商品總金額」
		// (商品總金額 + 運費 - 折價券折抵金額)
		Integer proTotal = finalProOrderVO.getProTotal();
		Integer proOrdShipFee = finalProOrderVO.getProOrdShipFee() != null ? finalProOrderVO.getProOrdShipFee() : 0;
		Integer proOrdCpndisc = finalProOrderVO.getProOrdCpndisc() != null ? finalProOrderVO.getProOrdCpndisc() : 0;

		// 可用來折抵的最高金額 (不含運費、已扣折價券)
		// 實務上通常點數不能折抵到 0 以下，甚至會限制不能折抵運費
		Integer maxDiscAmount = proTotal - proOrdCpndisc;
		Integer maxDiscPoint = maxDiscAmount;

		if (tempProOrdPointdisc > maxDiscPoint) {
			tempProOrdPointdisc = maxDiscPoint; // 限制最多只能折抵到商品總額
		}

		if (tempProOrdPointdisc < 0) {
			tempProOrdPointdisc = 0; // 限制最小折抵為 0
		}

		// 3. 計算並更新 ProOrderVO

		// 實際折抵金額 (假設 1 點 = 1 元)
		Integer pointDiscountAmount = tempProOrdPointdisc;

		// (1) 更新折抵點數
		finalProOrderVO.setProOrdPointdisc(pointDiscountAmount); // ⚠️ 注意：這裡儲存的是「金額」而不是「點數」 (根據您的 VO 命名判斷)

		// (2) 計算新的實付金額 (Grand Total)
		// 實付金額 = 商品總金額 + 運費 - 折價券折抵金額 - 點數折抵金額
		Integer proOrdGrandTotal = proTotal + proOrdShipFee - proOrdCpndisc - pointDiscountAmount;
		finalProOrderVO.setProOrdGrandTotal(proOrdGrandTotal);

		// (3) 計算新的回饋點數 (通常根據「商品總金額」或「實付金額」計算，這裡假設是根據實付金額)
		Integer proOrdPointGet = (int) (proOrdGrandTotal * PER);
		finalProOrderVO.setProOrdPointGet(proOrdPointGet);

		// 4. 將更新後的訂單物件存回 Session
		session.setAttribute("cartToProOrder", finalProOrderVO);

		// 5. 重定向回新增訂單頁面，讓頁面重新載入並顯示新的計算結果
		redirectAttributes.addFlashAttribute("successMessage", "點數折抵已更新！"); // 可選：顯示成功訊息

		// 必須使用 GET 重新導向到 addProOrder 才能正確顯示 (因為 addProOrder 是 @GetMapping)
		return "redirect:addProOrder";
		}
	}
}
