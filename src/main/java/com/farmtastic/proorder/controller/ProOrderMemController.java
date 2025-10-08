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
		// 從 ProOrderVO 中取出明細列表
		List<ProOrderItemVO> proOrderItemVO = finalProOrderVO.getProOrderItems();
		// 驗證成功後，新增資料
		proOrdSvc.addProOrder(finalProOrderVO, proOrderItemVO);
		// 因為要計算會員持有點數，還要寫一個修改Mem的service方法

		// 將資料交給資料庫

		return "/front_end/customer/logined/memProOrders/addProOrder";
	}
}
