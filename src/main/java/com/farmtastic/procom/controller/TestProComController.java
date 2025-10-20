package com.farmtastic.procom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.procom.dto.ProComByFmemIdDTO;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.procom.model.ProComVO;

@Controller
@RequestMapping("/procomtest")
public class TestProComController {

	@Autowired
	ProComService proComSvc;
	@Autowired
	MemService memSvc;

	// 測試首頁
	@GetMapping("/")
	public String index() {
		
		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@GetMapping("getAll")
	public String getAll(Model model) {

		List<ProComVO> list = proComSvc.getAll();
		System.out.println("查詢到的評論數量是: " + list.size()); // 新增這行！
		model.addAttribute("proComList", list);

		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@GetMapping("getOneByMemId")
	public String getIneByMemId(Model model, @RequestParam("memId") Integer memId) {

		Mem memVO = memSvc.getOneByMemId(memId);

		List<ProComVO> list = proComSvc.getProComByMemVO(memVO);

		System.out.println("查詢會員" + memVO.getMemName() + " 評論數量是: " + list.size()); // 新增這行！
		model.addAttribute("proComList", list);

		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@GetMapping("getOneByFmemId")
	public String getProComByFmemId(Model model, @RequestParam("fmemId") Integer fmemId) {

		List<ProComByFmemIdDTO> list = proComSvc.getProComByFmemId(fmemId);

		System.out.println("查詢會員評論數量是: " + list.size()); // 新增這行！
		model.addAttribute("proComListFmem", list);

		return "/front_end/customer/unlogined/procom/proComTest";
	}
	
	@GetMapping("countProRate")
	public String countProRate(Model model, @RequestParam("proId") Integer proId) {

		Integer count = proComSvc.countProComRateByProId(proId);

		System.out.println("查詢商品總分: " + count); // 新增這行！
		model.addAttribute("countPro", count);

		return "/front_end/customer/unlogined/procom/proComTest";
	}
	
	@GetMapping("countFmemRate")
	public String countFmemRate(Model model, @RequestParam("fmemId") Integer fmemId) {

		Integer countRate = proComSvc.countProComRateByFmemId(fmemId);
		Integer countCom = proComSvc.countProComByFmemId(fmemId);
//		System.out.println("查詢商品總分: " + count); // 新增這行！
		model.addAttribute("countFmemRate", countRate);
		model.addAttribute("countFmemCom", countCom);

		return "/front_end/customer/unlogined/procom/proComTest";
	}

}
