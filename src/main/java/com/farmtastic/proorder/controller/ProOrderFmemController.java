package com.farmtastic.proorder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		// 登入驗證交給 Fitter 攔截
		List<FmemOrderSummary> list = proOrdSvc.getAllByFmemId(loggedInFmember.getFmemId());
		model.addAttribute("proOrderList", list);

		return "/front_end/farmer/logined/fmemProOrders/listAllProOrder";

	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {
		
		// 查詢DB內的訂單以及訂單明細表格
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		List<ProOrderItemVO> items = ProOrderItemSvc.getProOrderItems(proOrderVO);
		// 放入 model 傳送至前端
		model.addAttribute("proOrderVO", proOrderVO);
		model.addAttribute("proOrderItems", items);
//		========================
		// 設定訂單狀態，給前端做下拉式選單
		Map<Integer, String> proOrdStatus = new HashMap<>();
		proOrdStatus.put(0, "成立訂單");
		proOrdStatus.put(1, "取消訂單(未出貨)");
		proOrdStatus.put(2, "出貨中");
		proOrdStatus.put(3, "已到貨");
		proOrdStatus.put(4, "申請退貨(已出貨)");
		proOrdStatus.put(5, "退貨中");
		proOrdStatus.put(6, "已退貨");
		// 放入 model 傳送至前端
		model.addAttribute("proOrdStatus",proOrdStatus);
//		========================
		// 設定付款狀態，給前端做下拉式選單
		Map<Integer, String> proPayStatus = new HashMap<>();
		proPayStatus.put(0, "未付款");
		proPayStatus.put(1, "已付款");
		// 放入 model 傳送至前端
		model.addAttribute("proPayStatus",proPayStatus);
		
		
		return "/front_end/farmer/logined/fmemProOrders/listOneProOrder";
	}

	// 小農會員修改訂各欄位的狀態
	@PostMapping("update")
	public String update(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		return null;
	}

}
