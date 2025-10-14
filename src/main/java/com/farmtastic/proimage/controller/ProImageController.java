package com.farmtastic.proimage.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pro-images")
public class ProImageController {

	@Autowired
	private ProImageService proImageService;

	@Autowired
	private ProService proService;

	/**
	 * 【修正】顯示指定產品的圖片管理頁面，並加入小農 session 驗證
	 * 
	 * @param proId 產品ID
	 */
	@GetMapping("/manage/{proId}")
	public String showImageManagementPage(@PathVariable("proId") Integer proId, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// 從 session 獲取登入的小農資訊
		Fmem loggedInFmem = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmem == null) {
			// 如果未登入，重導向到登入頁面
			return "redirect:/fmem/showFmemRegLoginForm";
		}

		Pro pro = proService.getOnePro(proId);

		// 驗證該商品是否屬於當前登入的小農
		if (pro == null || !Objects.equals(pro.getFmemId().getFmemId(), loggedInFmem.getFmemId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "您無權存取此商品！");
			return "redirect:/pro/fmem/myPro"; // 導回小農自己的商品列表
		}

		List<ProImage> images = proImageService.findAllImagesByProId(proId.longValue());

		model.addAttribute("pro", pro);
		model.addAttribute("imageList", images);
		// 使用 fmemProImage.html 作為小農專用的圖片管理頁面
		return "front_end/farmer/logined/fmemProfile/fmemProImage";
	}

	/**
	 * 為指定產品上傳新圖片
	 * 
	 * @param proId 產品ID
	 * @param file  上傳的圖片檔案
	 */
	@PostMapping("/upload")
	public String uploadImage(@RequestParam("proId") Integer proId, @RequestParam("imageFile") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "上傳失敗，請選擇一個檔案。");
			return "redirect:/pro-images/manage/" + proId;
		}

		try {
			ProImage newImage = new ProImage();
			newImage.setProId(proId.longValue());
			newImage.setProImg(file.getBytes());
			proImageService.createProductImage(newImage);
			redirectAttributes.addFlashAttribute("successMessage", "圖片上傳成功！");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "檔案讀取失敗，請稍後再試。");
		}

		return "redirect:/pro-images/manage/" + proId;
	}

	/**
	 * 刪除指定的圖片
	 * 
	 * @param proImgId 要刪除的圖片ID
	 * @param proId    該圖片所屬的產品ID，用於刪除後跳轉回原頁面
	 */
	@PostMapping("/delete")
	public String deleteImage(@RequestParam("proImgId") Long proImgId, @RequestParam("proId") Integer proId,
			RedirectAttributes redirectAttributes) {

		proImageService.deleteProImage(proImgId);
		redirectAttributes.addFlashAttribute("successMessage", "圖片刪除成功！");

		return "redirect:/pro-images/manage/" + proId;
	}
}