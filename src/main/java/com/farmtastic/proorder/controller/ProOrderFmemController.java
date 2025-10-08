package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.proorder.model.FmemOrderSummary;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/fmem/proorders")
public class ProOrderFmemController {

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		Integer fmemId = (Integer) session.getAttribute("fmemId");
		String fmemName = (String) session.getAttribute("fmemName");
		
		// 錯誤驗證
		if (loggedInFmember == null || fmemId == null || fmemName == null || fmemName.trim().isEmpty()) {
			// 沒有值則會重導至登入頁面
			return "redirect:/fmem/showFmemRegLoginForm";
		} else {
			try {
				List<FmemOrderSummary> list = proOrdSvc.getAllByFmemId(fmemId);
				model.addAttribute("proOrderList", list);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return "/front_end/farmer/logined/fmemProOrders/listAllProOrder";
		}
	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId")String proOrdId, ModelMap model) {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		List<ProOrderItemVO> items = ProOrderItemSvc.getProOrderItems(proOrderVO);

		model.addAttribute("proOrderVO", proOrderVO);
		model.addAttribute("proOrderItems", items);

		return "/front_end/farmer/logined/fmemProOrders/listOneProOrder";
	}

}
