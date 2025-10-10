package com.farmtastic.favopro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.favopro.model.FavoProServiceImp;
import com.farmtastic.member.model.Mem;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/favopro")
public class FavoProController {
	@Autowired
	FavoProServiceImp favoProSvc;

	@PostMapping("/add")
	public String addFavorite(@RequestParam Integer proId, HttpSession session, RedirectAttributes redirectAttributes) {
		// 從 Session 取出登入會員 檢查是否登入
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		if (loginUser == null) {
			redirectAttributes.addFlashAttribute("message", "請先登入會員才能收藏商品！");
			return "redirect:/mem/showMemRegLoginForm";
		}
		// 已登入 → 執行收藏流程
		Integer memId = loginUser.getMemId();

		try {
			// 檢查是否已收藏
			if (favoProSvc.isFavorite(memId, proId)) {
				redirectAttributes.addFlashAttribute("message", "該商品已收藏");
			} else {
				favoProSvc.addFavoPro(memId, proId);
				redirectAttributes.addFlashAttribute("message", "收藏成功！");
			}
			;
			// 回到商品詳情頁(不是正確的頁面)
			// return "frontend/logoned/product/product_detail";
			return "redirect:/frontend/logoned/product_test/detail?proId=" + proId;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "收藏失敗：" + e.getMessage());
			return "frontend/error_page";
		}
	}
}