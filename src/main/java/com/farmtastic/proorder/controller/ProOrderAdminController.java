package com.farmtastic.proorder.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.proorder.model.FmemOrderSummary;
import com.farmtastic.proorder.model.ProOrderService;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;
import com.mysql.cj.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;

@Controller
@RequestMapping("/admin/cashflow")
public class ProOrderAdminController {

	@Autowired
	ProOrderService proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;
	@Autowired
	FmemService fmemSvc;

	// 金流管理首頁
	@GetMapping("/")
	public String index(Model model, HttpSession session) {

		// 刪除 session 清空頁面
		session.removeAttribute("fmemId");

		return "/back_end/logined/cash_flow/index";
	}

	// 金流系統首頁導向搜尋商品訂單的功能
	@GetMapping("fmemProOrder")
	public String cashFlowIndexToSelectFmemProOrder(Model model) {

		// 計算訂單列表需要抽成的金額，
		proOrdSvc.calculateListsAllocTotal();

		model.addAttribute("fmemProOrder", "fmemProOrder");

		// 取該全部小農會員的id
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);

		return "/back_end/logined/cash_flow/index";
	}

	// 搜尋該小農的全部訂單

	@PostMapping("selectFmemProOrder")
	public String selectFmemProOrder(@RequestParam("fmemId") Integer fmemId, Model model, HttpSession session) {

		// 維持金流系統首頁是商品分支
		model.addAttribute("fmemProOrder", "fmemProOrder");

		// 取該小農可以撥款的表單（已出貨以及已退款）
		List<FmemOrderSummary> proOrderList = proOrdSvc.getAllByFmemIdCanAlloc(fmemId);
		model.addAttribute("proOrderList", proOrderList);

		// 取該小農的聯絡資訊
		Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
		model.addAttribute("fmemVO", fmem);

		// 進入詳細資料，按下回上一頁，保持列表為該小農的商品訂單列表
		session.setAttribute("fmemId", fmem.getFmemId());

		// 取該全部小農會員的id，給搜尋特定小農用。
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);

		return "/back_end/logined/cash_flow/index";
	}

	@PostMapping("alloc")
	public String alloc(@RequestParam("proOrdId") Integer proOrdId, Model model, HttpSession session,
			HttpServletRequest request) {

		System.err.println(proOrdId);
		// 修改為已撥款狀態
		proOrdSvc.updateAllocStatus(proOrdId);

		model.addAttribute("fmemProOrder", "fmemProOrder");

		// 取該小農的姓名
		Integer fmemId = (Integer) session.getAttribute("fmemId");
		Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
		model.addAttribute("fmemVO", fmem);

		// 取該小農可以撥款的表單（已出貨以及已退款）
		List<FmemOrderSummary> proOrderList = proOrdSvc.getAllByFmemIdCanAlloc(fmemId);
		model.addAttribute("proOrderList", proOrderList);

		// 取該全部小農會員的id
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);

		return "/back_end/logined/cash_flow/index";
	}

	// 查詢全部訂單
	@GetMapping("listAllProOrder")
	public String listAll(Model model,
			 HttpSession session) {

		// 取全部訂單傳送到前端
		List<ProOrderVO> proOrderList = proOrdSvc.getAll();
		model.addAttribute("proOrderList", proOrderList);

		// 取該全部小農會員的id
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);

		return "/back_end/logined/cash_flow/listAllProOrder";
	}

	// 查詢單筆訂單
//	@PostMapping("listOneProOrder")
//	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, Model model) {
//
//		if (proOrdId == null || proOrdId.trim().isEmpty()) {
//			// 沒有值則會重導至小農登入頁面
//			return "redirect:/mem/showMemRegLoginForm";
//		} else {
//
//			ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
//
//			List<ProOrderItemVO> items = ProOrderItemSvc.getProOrderItems(proOrderVO);
//
//			model.addAttribute("proOrderVO", proOrderVO);
//			model.addAttribute("proOrderItems", items);
//
//			return "/back_end/logined/cash_flow/listOneProOrder";
//		}
//	}
}