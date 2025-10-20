package com.farmtastic.news.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.farmtastic.news.model.NewsService;

import jakarta.servlet.http.HttpSession;

public class FmemNewsController {

	@Autowired
	private NewsService newsService;

	/**
	 * (已更新) 處理顯示小農消息頁面的請求
	 * 
	 * @param model   用於將資料傳遞給視圖
	 * @param session 用於獲取當前登入的小農 ID
	 * @return for_f_news 頁面的模板名稱
	 */
	@GetMapping("/for-farmer-news") // 您可以自訂此路徑
	public String showNewsForFarmer(Model model, HttpSession session) {

		// 從 session 中獲取登入時儲存的小農 ID
		// 我們假設您在登入成功時，已經執行了 session.setAttribute("farmerId", aFarmerObject.getId());
		Object farmerIdObject = session.getAttribute("farmerId");

		if (farmerIdObject instanceof Integer) {
			Integer currentFarmerId = (Integer) farmerIdObject;
			// 使用從 session 取得的 ID 來查詢該小農可見的所有消息
			model.addAttribute("farmerNewsList", newsService.getNewsForFarmer(currentFarmerId));
		} else {
			// 如果 session 中找不到有效的 ID (例如：未登入、session 過期)，則返回一個空列表
			// 這樣可以避免頁面出錯，並能顯示「目前沒有任何消息」的提示
			System.err.println("警告：在 session 中找不到 'farmerId'，使用者可能尚未登入。");
			model.addAttribute("farmerNewsList", Collections.emptyList());
		}

		return "for_f_news"; // 返回您提供的 HTML 檔案名稱
	}
}
