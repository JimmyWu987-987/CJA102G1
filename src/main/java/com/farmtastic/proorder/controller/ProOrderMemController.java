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

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService proOrderItemSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");

		// 錯誤驗證
		if (loggedInMember == null || memId == null || memName == null || memName.trim().isEmpty()) {
			// 沒有值則會重導至登入頁面
			return "redirect:/mem/showMemRegLoginForm";
		} else {
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

	// 進入新增訂單頁面
	@GetMapping("addProOrder")
	public String addProOrder(HttpSession session, ModelMap model) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");

		// 錯誤驗證
		if (loggedInMember == null || memId == null || memName == null || memName.trim().isEmpty()) {
			// 沒有值則會重導至登入頁面
			return "redirect:/mem/showMemRegLoginForm";
		} else {
			ProOrderVO proOrderVO = new ProOrderVO();
			ProOrderItemVO proOrderItemVO = new ProOrderItemVO();
			Mem memVO = new Mem();
			// 1. 設定 memVO 的 memId
			memVO.setMemId(memId);
			// 2. 將包含 memId 的 memVO 設定給 proOrderVO
			proOrderVO.setMemVO(memVO);
			
			// 將值回傳至前端thymeleaf
			model.addAttribute("memVO", memVO);
			model.addAttribute("proOrderVO", proOrderVO);
			model.addAttribute("proOrderItemVO", proOrderItemVO);
			
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}
	}
	// 新增訂單
	@PostMapping("insert")
	public String insert(@Valid ProOrderVO proOrderVO ,@Valid ProOrderItemVO proOrderItemVO,BindingResult result,HttpSession session, ModelMap model) {
		
		
		proOrderVO.setMemVO(null);
		
		// 輸入資料的錯誤驗證
		if(result.hasErrors())
		{
			// 數入資料錯誤，重新返回訂單頁面
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}
		// 驗證成功後，新增資料
		proOrdSvc.addProOrder(proOrderVO);
		proOrderItemSvc.addProOrderItem(proOrderItemVO);
		
		
		// 將資料交給資料庫
		
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}
	}


