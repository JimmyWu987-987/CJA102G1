package com.farmtastic.favopro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.favoact.model.FavoActServiceImp;
import com.farmtastic.favoact.model.FavoActVO;
import com.farmtastic.favopro.model.FavoProServiceImp;
import com.farmtastic.favopro.model.FavoProVO;
import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//收藏商品與活動 寫在這
@Controller
@RequestMapping("/mem/favo")
public class FavoProController {
	private final FavoProServiceImp favoProSvc;
	private final FavoActServiceImp favoActSvc;
	private final ProService productSvc;

	@Autowired
	public FavoProController(FavoProServiceImp favoProSvc, FavoActServiceImp favoActSvc, ProService productSvc) {
		this.favoProSvc = favoProSvc;
		this.favoActSvc = favoActSvc;
		this.productSvc = productSvc;
	}

//還未修好
	// ==== 商品收藏相關 ====
	// 新增商品收藏
	@PostMapping("/addFavoPro")
	public String addProFavorite(@RequestParam Integer proId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		System.out.print("Controller，頭");
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.呼叫 Service 新增收藏
		try {
			// 取得 Product，Product 中包含 FmemVO，進而取得 fmemId
			System.out.println("準備查商品 ID=" + proId);
			Pro product = productSvc.getOnePro(proId);
			System.out.println("查到商品：" + product);
			if (favoProSvc.isFavorite(memId, proId)) {
				System.out.println("該商品已收藏");
				redirectAttributes.addFlashAttribute("message", "該商品已收藏");
			} else {
				System.out.println("該商品開始收藏");
				favoProSvc.addFavoPro(memId, proId);
				System.out.print("Controller，新增收藏" + product);
				redirectAttributes.addFlashAttribute("successMessage", product.getProName() + "成功加入收藏");
			}
			String referer = request.getHeader("Referer"); // <--- 取得前一個頁面的 URL
			// 3.重導回收藏清單頁
			return "redirect:" + (referer != null ? referer : "/mem/favo/list");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "收藏失敗：" + e.getMessage());
			return "redirect:/mem/favo/list";
		}
	}

	// 移除商品收藏
	@PostMapping("/removeFavoPro")
	public String removeProFavorite(@RequestParam Integer proId, HttpSession session,
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
		return "redirect:/mem/favo/list";
	}

//====活動收藏相關=====
	// 新增活動收藏
	@PostMapping("/addFavoAct")
	public String addActFavorite(@RequestParam Integer actId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		try {
			if (favoActSvc.isFavorite(memId, actId)) {
				redirectAttributes.addFlashAttribute("message", "該活動已收藏");
			} else {
				favoActSvc.addFavoAct(memId, actId);
				redirectAttributes.addFlashAttribute("message", "活動收藏成功！");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "收藏失敗：" + e.getMessage());
		}
		return "redirect:/mem/favo/list";
	}

	/** 移除活動收藏 */
	@PostMapping("/removeFavoAct")
	public String removeActFavorite(@RequestParam Integer actId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		try {
			favoActSvc.removeFavoAct(memId, actId);
			redirectAttributes.addFlashAttribute("message", "活動已從收藏中移除！");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("message", "收藏不存在或已被刪除。");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "移除收藏時發生錯誤：" + e.getMessage());
		}
		return "redirect:/mem/favo/list";
	}

//=====收藏清單頁======
	@GetMapping("/list")
	public String listFavorite(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.呼叫 Service 查詢該會員所有收藏
		List<FavoProVO> favoProList = favoProSvc.getByMember(memId);
		List<FavoActVO> favoActList = favoActSvc.getByMember(memId);
		model.addAttribute("favoProList", favoProList);
		model.addAttribute("favoActList", favoActList);
		// 3.收藏清單頁
		return "front_end/customer/logined/favo/MyFavoList";
	}

}