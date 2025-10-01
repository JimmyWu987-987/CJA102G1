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

import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

@Controller
@RequestMapping("/admin/cashflow")
public class ProOrderAdminController {

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;

	// 查詢全部訂單
	@GetMapping("listAllProOrder")
	public String listAll(Model model) {

		List<ProOrderVO> list = proOrdSvc.getAll();

		model.addAttribute("proOrderList", list);

		return "/back_end/logined/cash_flow/listAllProOrder";
	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		if (proOrdId == null || proOrdId.trim().isEmpty()) {
			// 沒有值則會重導至小農登入頁面
			return "redirect:/mem/showMemRegLoginForm";
		} else {

			ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

			List<ProOrderItemVO> items = ProOrderItemSvc.getProOrderItems(proOrderVO);

			model.addAttribute("proOrderVO", proOrderVO);
			model.addAttribute("proOrderItems", items);

			return "/back_end/logined/cash_flow/listOneProOrder";
		}
	}

}
