package com.farmtastic.favopro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.favopro.model.FavoProServiceImp;
import com.farmtastic.member.model.Mem;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/favopro")
public class FavoProController {
	@Autowired
	FavoProServiceImp favoProSvc;

	@PostMapping("/add")
	public String addFavorite(@RequestParam Integer proId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.呼叫 Service 新增收藏
		try {
			if (favoProSvc.isFavorite(memId, proId)) {
				redirectAttributes.addFlashAttribute("message", "該商品已收藏");
			} else {
				favoProSvc.addFavoPro(memId, proId);
				redirectAttributes.addFlashAttribute("message", "收藏成功！");
			}
			;
			// 回到商品詳情頁(不是正確的頁面)
			// 3.重導回收藏清單頁
			return "redirect:/favo/products/list";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "收藏失敗：" + e.getMessage());
			return "redirect:/favo/products/list";
		}
	}

	@PostMapping("/remove")
	public String removeFavorite(@RequestParam Integer proId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.呼叫 Service 刪除收藏
		try {
			favoProSvc.removeFavoPro(memId, proId);
			redirectAttributes.addFlashAttribute("message", "已從收藏中移除！");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("message", "收藏不存在或已被刪除。");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "移除收藏時發生錯誤：" + e.getMessage());
		}

		// 3.重導回收藏清單頁
		return "redirect:/favo/products/list";
	}
}