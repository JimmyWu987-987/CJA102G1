package com.farmtastic.ses.controller;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/fmem/ses")
@SessionAttributes({ "sessionFmemSes" })
public class FmemSesController {

	@Autowired
	private SesService sesSvc;

	@Autowired
	private ActService actSvc;

	// 定義時間格式, 讓時間格式與前端 input type="time" (HH:mm) 相符

	@InitBinder
	public void initBinder(WebDataBinder binder) {

		// 註冊 java.sql.Date 編輯器 (處理 sesDate 和 regEnd 格式為 yyyy-MM-dd)
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(java.sql.Date.class, new CustomDateEditor(dateFormat, true) {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text == null || text.trim().isEmpty()) {
					setValue(null);
				} else {
					try {
						// 使用 SimpleDateFormat 解析為 java.util.Date
						java.util.Date utilDate = dateFormat.parse(text);
						// 轉換為 java.sql.Date 並設置值
						setValue(new java.sql.Date(utilDate.getTime()));
					} catch (Exception ex) {
						throw new IllegalArgumentException("日期格式無效，應為 yyyy-MM-dd: " + text, ex);
					}
				}
			}

			// 確保 Thymeleaf 顯示時也能正確格式化
			@Override
			public String getAsText() {
				Object value = getValue();
				if (value instanceof java.sql.Date) {
					return dateFormat.format((java.sql.Date) value);
				}
				return "";
			}
		});

		// 註冊 java.sql.Time 編輯器 (處理 sesStart 和 sesEnd 格式為 HH:mm)
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setLenient(false);

		binder.registerCustomEditor(java.sql.Time.class, new CustomDateEditor(timeFormat, true) {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text == null || text.trim().isEmpty()) {
					setValue(null);
				} else {
					try {
						// 將字串解析為 java.util.Date
						java.util.Date date = timeFormat.parse(text);
						// 轉換為 java.sql.Time
						setValue(new java.sql.Time(date.getTime()));
					} catch (Exception ex) {
						throw new IllegalArgumentException("時間格式無效，應為 HH:mm: " + text, ex);
					}
				}
			}

			@Override // 確保 Thymeleaf 顯示時也能正確格式化
			public String getAsText() {
				Object value = getValue();
				if (value instanceof java.sql.Time) {
					return timeFormat.format((java.sql.Time) value);
				}
				return "";
			}
		});
	}

	// 查已上架的活動 (新增場次用)
	@GetMapping("/listLaunchedAct")
	public String listLaunchedActForLaunch(HttpSession session, ModelMap model) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		if (fmem == null) {
			model.addAttribute("message", "請先登入小農頁面, 謝謝");
			return "redirect:/showFmemRegLoginForm"; // 導回小農登入頁
		}

		Integer fmemId = fmem.getFmemId();

		List<Act> actList = actSvc.findByFmemIdAndActLaunStat(fmemId, 1, Sort.by(Sort.Direction.ASC, "actId"));

		model.addAttribute("actList", actList);

		if (actList.isEmpty()) {
			model.addAttribute("message", "目前尚無已上架活動可新增場次");
		}

		return "front_end/farmer/logined/fmemSes/listLaunchedAct";
	}

	// ========== 小農新增場次 ==========
	@GetMapping("addSes/{actId}")
	public String showAddSesForm(@PathVariable Integer actId, ModelMap model, HttpSession session) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		if (fmem == null) {
			model.addAttribute("message", "請先登入小農頁面, 謝謝");
			return "redirect:/showFmemRegLoginForm"; // 導回小農登入頁
		}

		Act act = actSvc.getOneAct(actId).orElseThrow(() -> new RuntimeException("活動不存在"));
		session.setAttribute("sessionAct", act);

		java.sql.Date actStart = act.getActStart();
		java.sql.Date actEnd = act.getActEnd();

		model.addAttribute("ses", new Ses());
		model.addAttribute("actId", actId);
		model.addAttribute("actFee", act.getActFee());

		// 可選的場次日期的限制 >> 至少要當天的 7 天後 OR 活動開始日 (Max(活動開始日, 今天 + 7 天))
		java.util.Calendar cal7 = java.util.Calendar.getInstance();
		cal7.add(java.util.Calendar.DAY_OF_MONTH, 7); // 加7天
		java.sql.Date after7 = new java.sql.Date(cal7.getTimeInMillis());

		java.sql.Date minDate = (actStart.compareTo(after7) > 0) ? actStart : after7;
		model.addAttribute("minDate", minDate);
		model.addAttribute("maxDate", actEnd);

		// 計算報名截止日期的最小值 >> 至少要當天的 3 天後 且為場次日期的至少前2天
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.add(java.util.Calendar.DAY_OF_MONTH, 3); // 加3天
		java.sql.Date MinRegEnd = new java.sql.Date(cal.getTimeInMillis());

		// 傳遞給前端
		model.addAttribute("minRegEnd", MinRegEnd);
		model.addAttribute("actStart", act.getActStart());
		model.addAttribute("actEnd", act.getActEnd());
		return "front_end/farmer/logined/fmemSes/addSes";
	}

	// 額外資料. 關聯資料. 檔案上傳 → @RequestParam !!!!
	@PostMapping("insertSes")
	public String addSes(@Valid @ModelAttribute("ses") Ses ses, BindingResult result, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) throws IOException {

		// 處理空表單用
		if (result.hasErrors()) {
			return "front_end/farmer/logined/fmemSes/addSes";
		}

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		Act act = (Act) session.getAttribute("sessionAct"); // 取得該場次

		if (fmem == null) {
			model.addAttribute("errorMsg", "請先登入小農帳號才能新增活動！");
			return "redirect:/showFmemRegLoginForm";
		}

		if (act == null) {
			model.addAttribute("errorMsg", "查無此活動, 無法新增場次");
			return "front_end/farmer/logined/fmemSes/addSes";
		}

		ses.setAct(act);

		// 初始化非必填但可能需要預設值的欄位... 測試看看
		if (ses.getSesLaunStat() == null) {
			ses.setSesLaunStat(0); // 預設為 0 (未上架/預設值)
		}

		sesSvc.addSes(ses);

//		// 設置 Flash Attribute，用於 SweetAlert
		redirectAttributes.addFlashAttribute("successMessage", "已成功新增場次！");
//	
		return "redirect:/fmem/ses/listAllSesForFmem"; // 新增完後轉到場次一覽
	}

	// ========== 查小農自己的全部場次 ==========
	// 查全部
	@GetMapping("/listAllSesForFmem")
	public String listAllSesForFmem(HttpSession session, ModelMap model) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		if (fmem == null) {
			// 沒登入的防呆
			model.addAttribute("message", "請先登入小農頁面, 謝謝");
			// 導回小農登入頁
			return "redirect:/showFmemRegLoginForm";
		}
		Integer fmemId = fmem.getFmemId();

		// 塞自己的FmemId、依日期排序
		List<Ses> sesList = sesSvc.findSesWithActByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "sesDate"));
		// 用 Set 避免同一個活動重複更新資料庫
		Set<Integer> processedActIds = new HashSet<>();

		for (Ses ses : sesList) {

			Integer headCount = sesSvc.getHeadCount(ses.getSesId());
			ses.setHeadCountCache(headCount);

			Act act = ses.getAct();

			if (act != null) {
				Integer actId = act.getActId();

				if (!processedActIds.contains(actId)) {
					actSvc.persistActScores(actId);

					Integer totalScore = actSvc.getActScore(actId);
					act.setActScore(totalScore);

					Integer reviewCount = actSvc.getActCnt(actId);
					act.setActCnt(reviewCount);

					String avgScoreStr = actSvc.calculateAverageActScore(totalScore, reviewCount);
					act.setActAvgScore(avgScoreStr);

					processedActIds.add(actId);

				}
			}
		}

		model.addAttribute("sesList", sesList);

		if (sesList.isEmpty()) {
			model.addAttribute("message", "您尚未創建場次");
			return "front_end/farmer/logined/fmemSes/listAllSesForFmem"; // 沒有場次就回場次一覽
		}

		return "front_end/farmer/logined/fmemSes/listAllSesForFmem";
	}

	// ================= 上下架場次 ==================
	@PostMapping("/toggleLaunchStat")
	public String toggleLaunchStat(@RequestParam("sesId") Integer sesId, @RequestParam("targetStat") Integer targetStat, // 1:上架,
																															// 0:下架
			HttpSession session, RedirectAttributes redirectAttributes) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if (fmem == null) {
			return "redirect:/showFmemRegLoginForm";
		}
		Integer fmemId = fmem.getFmemId();

		Optional<Ses> sesOpt = sesSvc.getOneSes(sesId);

		if (sesOpt.isPresent()) {
			Ses ses = sesOpt.get();

			// 防呆權限檢查
			if (ses.getAct() == null || !ses.getAct().getFmem().getFmemId().equals(fmemId)) {
				redirectAttributes.addFlashAttribute("errorMessage", "您無權操作此場次");
				return "redirect:/fmem/ses/listAllSesForFmem";
			}

			Integer currentHeadCount = sesSvc.getHeadCount(sesId);

			// 進行上下架
			ses.setSesLaunStat(targetStat);
			ses.setSesLaunUpd(new Timestamp(System.currentTimeMillis()));

			String message;
			if (targetStat.equals(0)) {
				if (!currentHeadCount.equals(0)) {
					redirectAttributes.addFlashAttribute("errorMessage", "場次 ID " + sesId + " 已有 " + currentHeadCount + // 顯示正確人數
							" 人報名，無法執行「下架」操作。若需中止場次，請點擊「取消場次」。");
					return "redirect:/fmem/ses/listAllSesForFmem";

				}
				// 報名狀態為 改為 5 (未開始報名)
				ses.setRegStat(5);
				message = "場次 ID " + sesId + " 已完成下架";
			} else {
				// 1 (上架), 同時設定報名狀態為 0 (報名中)
				ses.setRegStat(0);
				message = "場次 ID " + sesId + " 已完成上架";
			}

			sesSvc.updateSes(ses, fmemId);
			redirectAttributes.addFlashAttribute("successMessage", message);

		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "找不到該場次 ID: " + sesId);
		}

		return "redirect:/fmem/ses/listAllSesForFmem";
	}

	// ================= 取消場次 (= 編輯報名狀態+下架) ==================
	@PostMapping("/cancelSes")
	public String cancelSes(@RequestParam("sesId") Integer sesId, HttpSession session, ModelMap model) {

		// 抓登入中小農
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		Integer fmemId = fmem.getFmemId();

		// 取得場次物件
		Optional<Ses> sesOpt = sesSvc.getOneSes(sesId);

		if (sesOpt.isPresent()) {
			Ses ses = sesOpt.get();

			ses.setRegStat(4); // 4 為已取消
			ses.setSesLaunStat(0); // 0 = 下架
			ses.setSesLaunUpd(new Timestamp(System.currentTimeMillis()));

			sesSvc.updateSes(ses, fmemId);

			model.addAttribute("successMessage", "場次 ID " + sesId + " 已成功取消並下架, 請務必通知報名者該場次已取消");

		} else {
			model.addAttribute("errorMessage", "找不到該場次 ID: " + sesId);
		}

		return "redirect:/fmem/ses/listAllSesForFmem";
	}
}