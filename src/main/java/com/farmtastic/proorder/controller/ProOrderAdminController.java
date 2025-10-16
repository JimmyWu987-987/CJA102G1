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
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class ProOrderAdminController {
	
	// 每筆訂單的抽成百分筆
	private static final double ALLOC_PER = 0.1;

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;
	@Autowired
	FmemService fmemSvc;
	
	// 金流管理首頁
	@GetMapping("admin/cashflow")
	public String index() {
		
		return "/back_end/logined/cash_flow/index.html";
	}

	// 查詢全部訂單
	@GetMapping("admin/cashflow/listAllProOrder")
	public String listAll(Model model) {
		
		
		// 計算訂單列表需要抽成的金額，
		List<ProOrderVO> CalculateListsAllocTotal = proOrdSvc.getAll();
		
//		CalculateListsAllocTotal.stream()
//								.filter(AllocTotal -> AllocTotal.getProOrdAllocTotal()==null)
//								.filter(AllocTotal -> AllocTotal.getProOrdAllocSendFmem()==null)
//								.forEach(null);
		
		for (ProOrderVO saveAllocTotal : CalculateListsAllocTotal) {
			
			// 判斷是否有需要更新資料
			boolean update = false;
			
			if(saveAllocTotal.getProOrdAllocTotal() == null) {
				// 依照訂單的商品總金額（不含運不含折扣），計算平台抽成的金額。
				Integer finalAllocTotal = (int) (saveAllocTotal.getProTotal() * ALLOC_PER);
				saveAllocTotal.setProOrdAllocTotal(finalAllocTotal);
				
				update = true;
			}
			
			if(saveAllocTotal.getProOrdAllocSendFmem() == null) {
				// 計算平台撥款金額
				Integer proOrdAllocSendFmem = saveAllocTotal.getProTotal() - saveAllocTotal.getProOrdAllocTotal();
				saveAllocTotal.setProOrdAllocSendFmem(proOrdAllocSendFmem);
				
				update = true;
			}
			
			
			// 如果有更新資料，才做更新。
			if(update) {		
				proOrdSvc.updateProOrder(saveAllocTotal);
			}
		}
		
//		// 取全部訂單傳送到前端
//		List<ProOrderVO> proOrderList = proOrdSvc.getAll();
//		model.addAttribute("proOrderList", proOrderList);
		
		// 取該全部小農會員的id
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);

		return "/back_end/logined/cash_flow/listAllProOrder";
	}

	// 查詢單筆訂單
	@PostMapping("admin/cashflow/listOneProOrder")
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
	
	// 金流系統首頁導向搜尋商品訂單的功能
	@PostMapping("admin/cashflow/fmemProOrder")
	public String cashFlowIndexToSelectFmemProOrder(ModelMap model) {
		
		model.addAttribute("fmemProOrder", "fmemProOrder");
		
		// 取該全部小農會員的id
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);
		
		return "/back_end/logined/cash_flow/index.html";
	}
	
	// 搜尋該小農的全部訂單
	@PostMapping("admin/cashflow/selectFmemProOrder")
	public String selectFmemProOrder(@RequestParam("fmemId") Integer fmemId, 
			ModelMap model,
			HttpSession session) {
		
		
		// 維持金流系統首頁是商品分支
		model.addAttribute("fmemProOrder", "fmemProOrder");
		
		// 取該小農可以撥款的表單（已出貨以及已退款）
		List<FmemOrderSummary> proOrderList = proOrdSvc.getAllByFmemIdCanAlloc(fmemId);
		model.addAttribute("proOrderList", proOrderList);
		
		// 取該小農的姓名
		Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
		model.addAttribute("fmemName", fmem.getFmemName());
		
		// 進入詳細資料，按下回上一頁，保持列表為該小農的商品訂單列表
		session.setAttribute("fmemId", fmem.getFmemId());
		
		// 取該全部小農會員的id，給搜尋特定小農用。
		List<Fmem> fmemList = fmemSvc.getAll();
		model.addAttribute("fmemList", fmemList);
		
		
		return "/back_end/logined/cash_flow/index.html";
	}

}