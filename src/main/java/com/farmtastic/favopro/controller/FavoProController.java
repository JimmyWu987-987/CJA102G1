package com.farmtastic.favopro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
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
	/** 頁面路徑常數 */
	private static final String REDIRECT_LIST = "redirect:/mem/favo/list";
	private static final String MY_FAVO_LIST = "front_end/customer/logined/favo/MyFavoList";

	private final FavoProServiceImp favoProSvc;
	private final FavoActServiceImp favoActSvc;
	private final ProService productSvc;
	private final ActService actSvc;

	@Autowired
	public FavoProController(FavoProServiceImp favoProSvc, FavoActServiceImp favoActSvc, ProService productSvc,
			ActService actSvc) {
		this.favoProSvc = favoProSvc;
		this.favoActSvc = favoActSvc;
		this.productSvc = productSvc;
		this.actSvc = actSvc;
	}

	// ============================================
	// 商品收藏相關
	// ============================================
	/**
	 * 新增商品收藏
	 */
	@PostMapping("/addFavoPro")
	public String addProFavorite(@RequestParam Integer proId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		System.out.print(" [收藏商品] Controller 開始執行");
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.呼叫 Service 新增收藏
		try {
			System.out.println("準備查商品 ID=" + proId);
			// 3.取得 Pro，Product 中 取得商品資料裡面有商品名稱
			Pro product = productSvc.getOnePro(proId);
			System.out.println("查到商品：" + product);
			if (favoProSvc.isFavorite(memId, proId)) {
				System.out.println("該商品已收藏");
				redirectAttributes.addFlashAttribute("successMessage", "該商品已收藏");
			} else {
				favoProSvc.addFavoPro(memId, proId);
				System.out.print("Controller，新增收藏" + product);
				redirectAttributes.addFlashAttribute("successMessage", product.getProName() + "成功加入收藏");
			}
			// 4.重導回前一頁（若無，則回收藏清單）
			String referer = request.getHeader("Referer"); // <--- 取得前一個頁面的 URL
			return "redirect:" + (referer != null ? referer : "/mem/favo/list");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "收藏失敗：" + e.getMessage());
			return REDIRECT_LIST;
		}
	}

	/**
	 * 移除商品收藏
	 */
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
		return REDIRECT_LIST;
	}

	// ============================================
	// 活動收藏相關
	// ============================================
	/**
	 * 新增活動收藏
	 */
	@PostMapping("/addFavoAct")
	public String addActFavorite(@RequestParam Integer actId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		System.out.println(" [收藏活動] Controller 開始執行");
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();

		// 2️.呼叫 Service 新增收藏
		try {
			// 3.查出活動資料（用於顯示活動名稱）
			System.out.println("準備查活動 ID = " + actId);
			Optional<Act> activityOpt = actSvc.getOneAct(actId);
			Act activity = activityOpt.get();
			System.out.println("查到活動：" + activity.getActName());
			if (favoActSvc.isFavorite(memId, actId)) {
				System.out.println("準備查活動 ID=" + actId);
				redirectAttributes.addFlashAttribute("successMessage", "該活動已收藏");
			} else {
				favoActSvc.addFavoAct(memId, actId);
				redirectAttributes.addFlashAttribute("successMessage", activity.getActName() + "成功加入收藏");
			}
			// 4.重導回前一頁（若無，則回收藏清單）
			String referer = request.getHeader("Referer"); // <--- 取得前一個頁面的 URL
			return "redirect:" + (referer != null ? referer : "/mem/favo/list");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "收藏失敗：" + e.getMessage());
		}
		return REDIRECT_LIST;

	}

	/**
	 * 移除活動收藏
	 */
	@PostMapping("/removeFavoAct")
	public String removeActFavorite(@RequestParam Integer actId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		try {
			// 2️.呼叫 Service 移除收藏
			favoActSvc.removeFavoAct(memId, actId);
			redirectAttributes.addFlashAttribute("message", "活動已從收藏中移除！");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("message", "收藏不存在或已被刪除。");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "移除收藏時發生錯誤：" + e.getMessage());
		}
		return REDIRECT_LIST;
	}

	// ============================================
	// 收藏清單頁
	// ============================================
	/**
	 * 顯示收藏清單（商品 + 活動）
	 */
	@GetMapping("/list")
	public String listFavorite(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		// 1.取得登入會員
		Mem loginUser = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loginUser.getMemId();
		// 2️.查詢收藏清單
		List<FavoProVO> favoProList = favoProSvc.getByMember(memId);
		List<FavoActVO> favoActList = favoActSvc.getByMember(memId);
		// 3️.放入 model 給前端 Thymeleaf 顯示
		model.addAttribute("favoProList", favoProList);
		model.addAttribute("favoActList", favoActList);
		// 4.返回收藏清單頁
		return MY_FAVO_LIST;
	}

}