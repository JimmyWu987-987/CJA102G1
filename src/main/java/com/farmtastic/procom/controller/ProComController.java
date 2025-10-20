package com.farmtastic.procom.controller;

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

import com.farmtastic.procom.model.ProComService;
import com.farmtastic.procom.model.ProComVO;
import com.farmtastic.proorder.model.ProOrderService;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem/proorders/procom")
public class ProComController {

	@Autowired
	ProComService proComSvc;
	@Autowired
	ProOrderService proOrdSvc;
	@Autowired
	ProOrderItemService proOrdItemSvc;

	// 測試首頁
	@GetMapping("/")
	public String index() {

		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@PostMapping("add")
	public String addProCom(@RequestParam("proOrdId") String proOrdId, ModelMap model) {
		// 查詢該訂單是否存在
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

		if (proOrderVO == null) {
			model.addAttribute("errorMessage", "查無此訂單！無法評價！");
			return "/front_end/customer/logined/memProOrders/listAllProOrder";
		}

		// 查詢該訂單之明細
		List<ProOrderItemVO> proOrdItemList = proOrdItemSvc.getProOrderItems(proOrderVO);

		ProComVO proComVO = new ProComVO();
		// 預設為該購買會員的評論
		proComVO.setMemVO(proOrderVO.getMemVO());

		// 預設評論時間為當下日期
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		proComVO.setProComTime(currentTimestamp);

		model.addAttribute("proOrdId", proOrdId);
		model.addAttribute("proOrdItemList", proOrdItemList);
		model.addAttribute("proComVO", proComVO);
		return "/front_end/customer/logined/proCom/addProCom";
	}

	@PostMapping("insert")
	public String insertProCom(@Valid ProComVO proComVO, BindingResult result, // 需要@Valid 的驗證，後面一定要接續BindingResult
			@RequestParam("proOrdId") String proOrdId, Model model) {

		// 查詢該訂單是否存在
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

		// 錯誤驗證
		// 驗證判斷寫在 ProComVO
		if (result.hasErrors()) {

			// 查詢該訂單之明細
			List<ProOrderItemVO> proOrdItemList = proOrdItemSvc.getProOrderItems(proOrderVO);
			// 重新回傳到新增頁面
			model.addAttribute("proOrdId", proOrdId);
			model.addAttribute("proOrdItemList", proOrdItemList);
			model.addAttribute("proComVO", proComVO);

			return "/front_end/customer/logined/proCom/addProCom";
		} else {
			// 新增評論至資料庫
			
			proComVO.setMemVO(proOrderVO.getMemVO());
			
			proComSvc.addProCom(proComVO);

			return "redirect:/mem/proorders/listAllProOrder";
		}
	}

}
