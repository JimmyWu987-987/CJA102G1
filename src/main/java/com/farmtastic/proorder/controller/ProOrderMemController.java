package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

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
	FmemService FemSvc;

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
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");
		ProOrderVO cartToProOrder = (ProOrderVO) session.getAttribute("cartToProOrder");

		// 如果 Model 中沒有 cartToProOrder (即非從結帳頁面重定向而來)，直接返回至首頁。
		// 這樣可防止用戶直接訪問此 URL 時發生錯誤
		if (cartToProOrder == null) {
			return "redirect:/";
		} else {
			// 將值回傳至前端thymeleaf
			model.addAttribute("memVO", loggedInMember);
			model.addAttribute("cartToProOrder", cartToProOrder);
//			model.addAttribute("fmemVO", fmem);
//			model.addAttribute("proOrderVO", proOrderVO);
//			model.addAttribute("proOrderItemVO", items);

			return "/front_end/customer/logined/memProOrders/addProOrder";

		}

	}

	// 新增訂單至DB
	@PostMapping("insert")
	public String insert(@Valid ProOrderVO proOrderVO, BindingResult result, HttpSession session, ModelMap model) {

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		ProOrderVO finalProOrderVO = (ProOrderVO) session.getAttribute("cartToProOrder");

		// 輸入資料的錯誤驗證
//		if (result.hasErrors()) {
//			// 如果有錯誤，將原始的 cartToProOrder 和其他必要資料重新傳回頁面
//			model.addAttribute("memVO", loggedInMember);
//			model.addAttribute("cartToProOrder", finalProOrderVO);
//			return "/front_end/customer/logined/memProOrders/addProOrder";
//		}
	    // 🌟 關鍵修正：確保 proOrderVO 裡面的明細列表是正確的 🌟
	    List<ProOrderItemVO> itemsToSave = finalProOrderVO.getProOrderItems();
	    
	    // 檢查明細列表是否為空
	    if (itemsToSave == null || itemsToSave.isEmpty()) {
	        // 處理錯誤，例如重定向回購物車頁面
	        model.addAttribute("errorMessage", "購物車是空的，無法新增訂單！");
	        return "/front_end/customer/logined/memProOrders/addProOrder"; // 或其他錯誤頁面
	    }
		
	    // 2. 合併資料：將計算好的金額/折扣設定給 proOrderVO (表單提交的)
	    // 🌟 關鍵修正：將所有可能為 NULL 的金額屬性從 finalProOrderVO 複製過來 🌟
	    proOrderVO.setProOrdDate(finalProOrderVO.getProOrdDate());
	    proOrderVO.setProOrdCpndisc(finalProOrderVO.getProOrdCpndisc() != null ? finalProOrderVO.getProOrdCpndisc() : 0);
	    proOrderVO.setProOrdGrandTotal(finalProOrderVO.getProOrdGrandTotal());
	    proOrderVO.setProTotal(finalProOrderVO.getProTotal());
	    proOrderVO.setProOrdShipFee(finalProOrderVO.getProOrdShipFee());
	    proOrderVO.setProOrdPointdisc(finalProOrderVO.getProOrdPointdisc() != null ? finalProOrderVO.getProOrdPointdisc() : 0);
	    proOrderVO.setProOrdPointGet(finalProOrderVO.getProOrdPointGet() != null ? finalProOrderVO.getProOrdPointGet() : 0);
	    
	    // 3. 設定關聯和明細
	    proOrderVO.setMemVO(loggedInMember);
	    proOrderVO.setProOrderItems(itemsToSave); 

	    // 4. 呼叫 Service 進行新增
	    // 注意：這裡將表單提交的 proOrderVO 和從 Session 來的明細列表傳入
	    // 這樣 Service 就能處理完整的訂單資訊。

	    try {
	        // proOrdSvc 是 ProOrderSevice 的實例
	        proOrdSvc.addProOrder(proOrderVO, itemsToSave); 
	        
	        // 清除 Session 相關屬性
	        session.removeAttribute("cartToProOrder");
	        
	    } catch (RuntimeException e) {
	        // 捕捉 Service 拋出的商品 ID 缺失或其他錯誤
	        model.addAttribute("errorMessage", "新增訂單失敗：" + e.getMessage());
	        model.addAttribute("memVO", loggedInMember);
	        model.addAttribute("cartToProOrder", finalProOrderVO);
	        return "/front_end/customer/logined/memProOrders/addProOrder";
	    }

		return "redirect:/mem/proorders/listAllProOrder";
	}
}
