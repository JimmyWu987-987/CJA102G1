package com.farmtastic.act.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.enums.ActStat;
import com.farmtastic.act.enums.LaunStat;
import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActCate;
import com.farmtastic.act.model.ActCateRepository;
import com.farmtastic.act.model.ActImg;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesRepository;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/fmem/act")
@SessionAttributes({ "sessionFmemAct" })
public class FmemActController {

	@InitBinder("act")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("actStart", "actEnd");
	}

	@Autowired
	private ActCateRepository actCateRepo;

	@Autowired
	private ActService actSvc;

	@Autowired
	private SesService sesSvc;

	@Autowired
	private SesRepository sesRepo;

	// =========== 編輯活動 ============
	@GetMapping("updateAct/{actId}")
	public String getUpdatePage(@PathVariable Integer actId, ModelMap model, RedirectAttributes redirectAttributes,
			HttpSession session) {

		// 取得活動
		Optional<Act> optAct = actSvc.getOneAct(actId);

		if (optAct.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "查無活動資料, 無法進行編輯");
			return "redirect:/fmem/act/listAllActForFmem";
		}

		// 確保小農是編輯自己的活動
		Act act = optAct.get();

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		Integer fmemId = fmem.getFmemId();
		if (fmem == null || !act.getFmemId().equals(fmemId)) {
			redirectAttributes.addFlashAttribute("errorMessage", "您沒有權限編輯此活動");
			return "redirect:/fmem/act/listAllActForFmem";
		}

		// 設定只能編輯尚未審核/審核未通過的活動
		if (act.getActStat().equals(2)) {
			redirectAttributes.addFlashAttribute("errorMessage", "僅能編輯尚未審核/審核未通過的活動");
			return "redirect:/fmem/act/listAllActForFmem";
		}

		model.addAttribute("act", act);

		List<ActCate> allCategories = actCateRepo.findAll();
		model.addAttribute("allCategories", allCategories);

		// 取得活動已選擇的分類ID (供 th:checked 使用，將 ActCate 轉為 List<Integer>)
		List<Integer> selectedCateIds = act.getActCate().stream().map(ActCate::getActCateId)
				.collect(Collectors.toList());
		model.addAttribute("actCateId", selectedCateIds);

		return "front_end/farmer/logined/fmemAct/updateAct";
	}

	@PostMapping("/update")
	public String updateAct(@RequestParam("actStart") String actStartStr, @RequestParam("actEnd") String actEndStr,
			@Valid @ModelAttribute("act") Act updatedAct, // 表單傳來的資訊
			BindingResult result,
			@RequestParam(value = "actMainImgFile", required = false) MultipartFile actMainImgFile,
			@RequestParam(value = "actImgs", required = false) MultipartFile[] actImgs,
			@RequestParam(value = "actCateId", required = false) List<Integer> actCateId, HttpSession session,
			Model model, RedirectAttributes redirectAttributes) throws IOException {

		// 初始化 model 資料
		List<ActCate> allCategories = actCateRepo.findAll();
		model.addAttribute("allCategories", allCategories);
		model.addAttribute("actCateId", actCateId != null ? actCateId : new ArrayList<Integer>());

		// 取得原act物件
		Optional<Act> originOpt = actSvc.getOneAct(updatedAct.getActId());
		if (originOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "活動資料遺失，無法更新！");
			return "front_end/farmer/logined/fmemAct/updateAct";
		}
		Act originAct = originOpt.get();

		if (result.hasErrors()) {
			return "front_end/farmer/logined/fmemAct/updateAct";
		}

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農

		if (fmem == null) {
			model.addAttribute("errorMsg", "請先登入小農帳號才能編輯活動！");
			return "redirect:/showFmemRegLoginForm";
		}

		Integer fmemId = fmem.getFmemId();

		// 處理空表單用 (應該不會用到)
		// 不是空的再進行以下手動驗證
		// 先確保為sql的格式不是util的...
		java.sql.Date actStart = (actStartStr == null || actStartStr.isBlank()) ? null
				: java.sql.Date.valueOf(actStartStr);

		java.sql.Date actEnd = (actEndStr == null || actEndStr.isBlank()) ? null : java.sql.Date.valueOf(actEndStr);

		updatedAct.setActStart(actStart);
		updatedAct.setActEnd(actEnd);

		// 不選分類的驗證
		if (actCateId == null || actCateId.isEmpty()) {
			result.rejectValue("actCate", null, "請至少選擇一項分類");
		} else {
			model.addAttribute("actCateId", actCateId);
			Set<ActCate> cates = new HashSet<>();
			for (Integer id : actCateId) {
				ActCate cate = actCateRepo.findById(id).orElse(null);
				if (cate != null)
					cates.add(cate);
			}
			updatedAct.setActCate(cates);
		}

		// 開始日期的其他驗證
		if (actStart == null) { // 檢查新的開始日期
			result.rejectValue("actStart", null, "請填入活動開始日期");
		} else {
			java.sql.Date after45 = new java.sql.Date(System.currentTimeMillis() + 45L * 24 * 60 * 60 * 1000);
			if (actStart.before(after45)) {
				result.rejectValue("actStart", null, "考慮到審核作業時間及消費者報名時間, 僅能選擇 45 天之後的日期。");
			}
		}

		// 結束日期的其他驗證
		if (actEnd == null) { // 檢查新的結束日期
			result.rejectValue("actEnd", null, "請填入活動結束日期");
		} else if (actStart != null && actEnd != null && actEnd.before(actStart)) {
			result.rejectValue("actEnd", null, "結束日期不得早於開始日期。");
		}

		// 主圖
		if (actMainImgFile != null && !actMainImgFile.isEmpty()) {
			// 有上傳新圖 >> 執行驗證&替換
			if (!actMainImgFile.getContentType().startsWith("image/") || actMainImgFile.getSize() > 4 * 1024 * 1024) {
				result.rejectValue("actMainImg", null, "圖片格式或大小不符 (須為圖片且小於 4MB)");
			} else {
				try {
					updatedAct.setActMainImg(actMainImgFile.getBytes());
				} catch (IOException e) {
					result.rejectValue("actMainImg", null, "讀取主要圖片發生 IO 錯誤，請重試。");
				}
			}
		} else {
			byte[] originalMainImg = originAct.getActMainImg();
			if (originalMainImg == null || originalMainImg.length == 0) {
				result.rejectValue("actMainImg", null, "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
			}
			updatedAct.setActMainImg(originalMainImg);
		}

		// 其他圖片驗證
		// 要先初始化, 避免沒有放活動圖時報錯
		if (actImgs != null && actImgs.length > 0 && Arrays.stream(actImgs).anyMatch(file -> !file.isEmpty())) {
			int count = 0;
			for (MultipartFile file : actImgs) {
				if (!file.isEmpty()) {
					count++;
				}
			}

			int order = 1;
			for (MultipartFile file : actImgs) {
				if (!file.isEmpty()) {
					if (!file.getContentType().startsWith("image/")) {
						result.rejectValue("actImgs", null, "所有檔案都必須是圖片");
						break;
					} else if (file.getSize() > 4 * 1024 * 1024) {
						result.rejectValue("actImgs", null, "每張圖片不得超過 4MB");
						break;
					} else {
						ActImg actImg = new ActImg();
						actImg.setActImg(file.getBytes());
						actImg.setActimgOrder(order++); // 存順序用的
						actImg.setAct(updatedAct);
						updatedAct.getActImg().add(actImg);
					}
				}
			}

		} else {
			updatedAct.setActImg(originAct.getActImg());
			for (ActImg img : updatedAct.getActImg()) {
				img.setAct(updatedAct);
			}
		}

		// 維持原始資料的欄位
		updatedAct.setFmemId(originAct.getFmemId());
		updatedAct.setActLaunStat(originAct.getActLaunStat());
		updatedAct.setActScore(originAct.getActScore());
		updatedAct.setActCnt(originAct.getActCnt());
		updatedAct.setActRemark(originAct.getActRemark()); // 審核備註要保留

		// 更新狀態
		updatedAct.setActStat(4); // 設為"已編輯送審, 待審核" >> 4
		updatedAct.setActUpd(new Timestamp(System.currentTimeMillis()));

		// 若驗證又有錯誤就再傳回
		if (result.hasErrors()) {
			model.addAttribute("act", updatedAct);

			model.addAttribute("allCategories", allCategories);

			// 確保 actCateId 在錯誤時能回填 (從 updatedAct 取得)
			if (updatedAct.getActCate() != null) {
				List<Integer> selectedCateIds = updatedAct.getActCate().stream().map(ActCate::getActCateId)
						.collect(Collectors.toList());
				model.addAttribute("actCateId", selectedCateIds);
			}

			return "redirect:/fmem/act/updateAct";
		}

		// 執行更新
		actSvc.updateAct(updatedAct, fmemId);

		redirectAttributes.addFlashAttribute("successMessage", "活動資料已更新並重新送審！");

		return "redirect:/fmem/act/listAllActForFmem";
	}

	// =========== 小農查詢自己的活動 (ok) ============

	// 查全部
	@GetMapping("/listAllActForFmem")
	public String listAllActForFmem(HttpSession session, ModelMap model) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		Integer fmemId = fmem.getFmemId();

		// 塞自己的FmemId
		List<Act> actList = actSvc.findByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "actId"));

		for (Act act : actList) {
			Integer actId = act.getActId();

			actSvc.persistActScores(actId);

			Integer totalScore = actSvc.getActScore(actId);
			Integer reviewCount = actSvc.getActCnt(actId);

			act.setActScore(totalScore);
			act.setActCnt(reviewCount);

			String avgScoreStr = actSvc.calculateAverageActScore(totalScore, reviewCount);
			act.setActAvgScore(avgScoreStr);
		}

		model.addAttribute("actList", actList);

		if (actList.isEmpty()) {
			model.addAttribute("message", "目前尚無活動");
		}

		return "front_end/farmer/logined/fmemAct/listAllActForFmem";
	}

	// 查已過審的活動 (上下架用)
	@GetMapping("/launchAct")
	public String listApprovedForLaunch(HttpSession session, ModelMap model) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		Integer fmemId = fmem.getFmemId();

		// 塞自己的FmemId
		List<Act> actList = actSvc.findByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "actId"));

		for (Act act : actList) {
			Integer actId = act.getActId();

			actSvc.persistActScores(actId);

			Integer totalScore = actSvc.getActScore(actId);
			Integer reviewCount = actSvc.getActCnt(actId);

			act.setActScore(totalScore);
			act.setActCnt(reviewCount);

			String avgScoreStr = actSvc.calculateAverageActScore(totalScore, reviewCount);
			act.setActAvgScore(avgScoreStr);
		}

		model.addAttribute("actList", actList);

		if (actList.isEmpty()) {
			model.addAttribute("message", "目前尚無已過審活動可進行上下架");
		}

		return "front_end/farmer/logined/fmemAct/launchAct";
	}

	// ============ 單一查詢 (for 活動詳細頁面用) ============
	@GetMapping("/detail/{actId}")
	public String actDetailsForFmem(HttpSession session, @PathVariable Integer actId, ModelMap model) {

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		if (fmem == null) {
			// 沒登入的防呆
			model.addAttribute("message", "請先登入小農頁面, 謝謝");
			// 導回小農登入頁
			return "redirect:/showFmemRegLoginForm";
		}

		Integer fmemId = fmem.getFmemId();

		Optional<Act> optAct = actSvc.getOneActByFmemId(actId, fmemId); // 取得活動

		// 防呆用
		if (optAct.isEmpty()) {
			// 查無活動 > 導回首頁或活動一覽頁，顯示訊息
			model.addAttribute("message", "查無此活動");
			return "front_end/customer/unlogined/act/actMainPageTest"; // 做一個 "查無此活動" 頁面 or 導回首頁
		}

		Act act = optAct.get();

		actSvc.persistActScores(actId);

		Integer totalScore = actSvc.getActScore(actId);
		Integer reviewCount = actSvc.getActCnt(actId);

		act.setActScore(totalScore);
		act.setActCnt(reviewCount);

		String avgScoreStr = actSvc.calculateAverageActScore(totalScore, reviewCount);
		act.setActAvgScore(avgScoreStr);

		if (act.getActImg() != null) {
			act.getActImg().size();
		}

		// 依分類ID排序
		List<ActCate> sortedCate = new ArrayList<>(act.getActCate());
		sortedCate.sort(Comparator.comparing(ActCate::getActCateId));
		model.addAttribute("actCateList", sortedCate);

		// 先依場次日期在依場次時間升冪排序
		Sort sort = Sort.by(Sort.Direction.ASC, "sesDate").and(Sort.by(Sort.Direction.ASC, "sesStart"));

		List<Ses> allSes = sesSvc.findSesByActId(actId, sort);
		List<Ses> launchedSes = allSes.stream().filter(s -> s.getSesLaunStat() != null).toList();

		for (Ses ses : launchedSes) {
			// 呼叫 SesService 取得報名人數
			Integer headCount = sesSvc.getHeadCount(ses.getSesId());

			// 將計算結果設定到 Ses 物件中。
			// 注意：您需要在 Ses.java 中新增 setActualRegCount() 方法
			ses.setHeadCountCache(headCount);
		}

		model.addAttribute("sesList", launchedSes);
		model.addAttribute("act", act);
		model.addAttribute("sessionAct", act);

		return "front_end/farmer/logined/fmemAct/actDetailsForFmem";

	}

	// =========== 上下架活動 ============
	@PostMapping("/updateLaunchStatus/{actId}")
	public String updateLaunchStatus(@PathVariable Integer actId, @RequestParam("newLaunStat") Integer newLaunStat,
			HttpSession session, RedirectAttributes redirectAttributes) {

		// 防呆, 一樣先取小農&單一活動
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");

		if (fmem == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "請先登入小農頁面, 謝謝");
			return "redirect:/showFmemRegLoginForm";
		}

		Integer fmemId = fmem.getFmemId();

		try {
			Optional<Act> optAct = actSvc.getOneActByFmemId(actId, fmemId);

			if (optAct.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "查無此活動");
				return "redirect:/fmem/act/launchActList";
			}

			Act act = optAct.get();

			// 僅能對已審核通過的進行上下架
			if (act.getActStat() != 2) {
				redirectAttributes.addFlashAttribute("errorMessage", "只有已過審核的活動才能進行上下架操作");
				return "redirect:/fmem/act/detail/{actId}";
			}

			// 若有上架中的場次, 就不能對活動進行上下架
			if (newLaunStat == 0) { // 僅在執行「下架」操作 (newLaunStat=0) 時才檢查

				// 查詢活動的所有場次
				Sort sort = Sort.by(Sort.Direction.ASC, "sesDate").and(Sort.by(Sort.Direction.ASC, "sesStart"));
				List<Ses> allSes = sesSvc.findSesByActId(actId, sort);

				// 過濾出 "上架中" 的場次 (假設 SesLaunStat=1 表示上架)
				boolean hasLaunchedSes = allSes.stream()
						.anyMatch(s -> s.getSesLaunStat() != null && s.getSesLaunStat() == 1);

				if (hasLaunchedSes) {
					redirectAttributes.addFlashAttribute("errorMessage", "此活動尚有上架中的場次，請先將所有場次下架或完成所有場次, 才能下架整個活動");
					return "redirect:/fmem/act/detail/{actId}"; // 導回詳情頁
				}
			}

			// set 活動上下架狀態 & 活動上下架狀態更新時間
			act.setActLaunStat(newLaunStat);
			act.setActLaunUpd(new Timestamp(System.currentTimeMillis()));

			// 進行更新
			actSvc.updateAct(act, fmemId);

			String action = (newLaunStat == 1) ? "上架" : "下架"; // 上架是1
			redirectAttributes.addFlashAttribute("successMessage", "活動 ID: " + actId + " 已成功 " + action);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "更新上下架狀態時發生系統錯誤。");
		}

		return "redirect:/fmem/act/detail/" + actId;
	}

	// =========== 抓圖 ============

	// get主圖
	@GetMapping("/mainImg/{actId}")
	public void getActMainImg(@PathVariable Integer actId, HttpServletResponse response) {
		Optional<Act> optAct = actSvc.getOneAct(actId); // 取得活動

		if (optAct.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		}

		byte[] actMainImg;

		Act act = optAct.get();

		if (act.getActMainImg() == null) {
			actMainImg = new byte[0]; // 空白圖
		} else {
			actMainImg = optAct.get().getActMainImg();
		}

		writeImageToResponse(actMainImg, response);
	}

	// get其他活動圖
	@GetMapping("/img/{actId}/{imgId}")
	public void getActImg(@PathVariable Integer actId, @PathVariable Integer imgId, HttpServletResponse response) {
		Optional<Act> optAct = actSvc.getOneAct(actId); // 取得活動

		if (optAct.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		}

		Act act = optAct.get();

		if (act.getActImg() == null || act.getActImg().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		}

		ActImg img = act.getActImg().stream().filter(i -> i.getActImgId().equals(imgId)).findFirst().orElse(null);

		if (img == null || img.getActImg() == null || img.getActImg().length == 0) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		}

		writeImageToResponse(img.getActImg(), response);
	}

	private void writeImageToResponse(byte[] imgBytes, HttpServletResponse response) {

		// 檢查圖片位元組資料是否為空
		if (imgBytes == null || imgBytes.length == 0) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
			return;
		}

		try {
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

			response.getOutputStream().write(imgBytes);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
			// 修正錯誤狀態碼：寫入流時發生錯誤，應該返回 500
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// ============ 輪播器圖片 ============
	@GetMapping("/carousel/{actId}")
	@ResponseBody
	public byte[][] getCarouselImages(@PathVariable Integer actId) {
		List<byte[]> imgs = actSvc.getAllActImagesForCarousel(actId);
		return imgs.toArray(new byte[0][]);
	}

//  =========== 新增活動 ============
	@GetMapping("addAct")
	public String showAddActForm(ModelMap model, HttpSession session) {
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農

		if (fmem == null) {
			model.addAttribute("errorMsg", "請先登入小農帳號才能新增活動！");
			return "redirect:/showFmemRegLoginForm";
		}
		model.addAttribute("act", new Act());
		// 將分類丟給前端使用
		List<ActCate> allCategories = actCateRepo.findAll();
		model.addAttribute("allCategories", allCategories);

		// 【強制修正】：確保 actCateId 在 Model 中存在，避免前端 th:checked 崩潰
		model.addAttribute("actCateId", new ArrayList<Integer>());

		return "front_end/farmer/logined/fmemAct/addAct";
	}

	// 額外資料. 關聯資料. 檔案上傳 → @RequestParam !!!!
	@PostMapping("insert")
	public String addAct(@RequestParam("actStart") String actStartStr, @RequestParam("actEnd") String actEndStr,
			@Valid @ModelAttribute("act") Act act, BindingResult result,
			@RequestParam("actMainImgFile") MultipartFile actMainImgFile, // 1019 追加
//						 @RequestParam("actMainImg") MultipartFile actMainImg,
			@RequestParam(value = "actImgs", required = false) MultipartFile[] actImgs,
			@RequestParam(value = "actCateId", required = false) List<Integer> actCateId, HttpSession session,
			Model model, RedirectAttributes redirectAttributes) throws IOException {

		List<ActCate> allCategories = actCateRepo.findAll();
		model.addAttribute("allCategories", allCategories);

//		// 處理空表單用
//		if (result.hasErrors()) {
//			return "redirect:/fmem/act/addAct";
//		}

		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農

		if (fmem == null) {
			model.addAttribute("errorMsg", "請先登入小農帳號才能新增活動！");
			return "redirect:/showFmemRegLoginForm";
		}

		// 不是空的再進行以下手動驗證
		// 先確保為sql的格式不是util的...
		java.sql.Date actStart = (actStartStr == null || actStartStr.isBlank()) ? null
				: java.sql.Date.valueOf(actStartStr);

		java.sql.Date actEnd = (actEndStr == null || actEndStr.isBlank()) ? null : java.sql.Date.valueOf(actEndStr);

		act.setActStart(actStart);
		act.setActEnd(actEnd);

//		// 字數驗證
//		if (act.getActName() == null || act.getActName().trim().isEmpty()) {
//			result.rejectValue("actName", null, "活動名稱必需在{min}到{max}字之間");
//			return "redirect:/fmem/act/addAct";
//		} else if (act.getActName()) {
//		}
		
		
		
		// 不選分類的驗證
		if (actCateId == null || actCateId.isEmpty()) {
			result.rejectValue("actCate", null, "請至少選擇一項分類");
		} else {
			model.addAttribute("actCateId", actCateId);
			Set<ActCate> cates = new HashSet<>();
			for (Integer id : actCateId) {
				ActCate cate = actCateRepo.findById(id).orElse(null);
				if (cate != null)
					cates.add(cate);
			}
			act.setActCate(cates);
		}

		// 開始日期的其他驗證
//		java.sql.Date actStart = act.getActStart();

		if (act.getActStart() == null) {
			result.rejectValue("actStart", null, "請填入活動開始日期");
		} else {
			java.sql.Date after45 = new java.sql.Date(System.currentTimeMillis() + 45L * 24 * 60 * 60 * 1000);
			if (actStart != null && actStart.before(after45)) {
				result.rejectValue("actStart", null, "考慮到審核作業時間及消費者報名時間, 僅能選擇 45 天之後的日期。");
			}
		}

		// 結束日期的其他驗證
//		java.sql.Date actEnd = act.getActEnd();
		if (act.getActEnd() == null) {
			result.rejectValue("actEnd", null, "請填入活動結束日期");
		} else if (actStart != null && actEnd != null && actEnd.before(actStart)) {
			result.rejectValue("actEnd", null, "結束日期不得早於開始日期。");
		}

		// 1019 主圖修改
		if (actMainImgFile == null || actMainImgFile.isEmpty()) {
			result.rejectValue("actMainImg", null, "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
		} else if (!actMainImgFile.getContentType().startsWith("image/")) {
			result.rejectValue("actMainImg", null, "只能上傳圖檔");
		} else if (actMainImgFile.getSize() > 4 * 1024 * 1024) {
			result.rejectValue("actMainImg", null, "圖片不得超過 4MB");
		} else {
			try {
				act.setActMainImg(actMainImgFile.getBytes());
			} catch (IOException e) {
				result.rejectValue("actMainImg", null, "讀取主要圖片發生 IO 錯誤，請重試。");
			}
		}

		// 其他圖片驗證
		// 要先初始化, 避免沒有放活動圖時報錯
		if (act.getActImg() == null) {
			act.setActImg(new ArrayList<>());
		}

		if (actImgs != null && actImgs.length > 0) {
			// 上傳照片時, 先計算數量
			int count = 0;
			for (MultipartFile file : actImgs) {
				if (!file.isEmpty()) {
					count++;
				}
			}

			int order = 1;
			for (MultipartFile file : actImgs) {
				if (!file.isEmpty()) {
					if (!file.getContentType().startsWith("image/")) {
						result.rejectValue("actImgs", null, "所有檔案都必須是圖片");
						break;
					} else if (file.getSize() > 4 * 1024 * 1024) {
						result.rejectValue("actImgs", null, "每張圖片不得超過 4MB");
						break;
					} else {
						ActImg actImg = new ActImg();
						actImg.setActImg(file.getBytes());
						actImg.setActimgOrder(order++); // 存順序用的
						actImg.setAct(act);
						act.getActImg().add(actImg);
					}
				}
			}
		}

		if (result.hasErrors()) {
			if (actCateId == null || actCateId.isEmpty()) {
				model.addAttribute("actCateId", new ArrayList<Integer>());
			} else {
				model.addAttribute("actCateId", actCateId);
			}
            return "front_end/farmer/logined/fmemAct/addAct";
        }
		
//		// 若驗證又有錯誤就再傳回
//		if (result.hasErrors()) {
//            return "fmem/act/addAct"; 
//        }
		
		// 設定狀態&更新時間
		// (抓登入中的小農)
//		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
		Integer fmemId = fmem.getFmemId();
		act.setFmemId(fmemId);
		act.setActStat(1); // 設為待審核
		act.setActUpd(new Timestamp(System.currentTimeMillis()));

		// 初始化非必填但可能需要預設值的欄位... 測試看看
		if (act.getActLaunStat() == null) {
			act.setActLaunStat(0); // 預設為 0 (未上架/預設值)
		}
		if (act.getActScore() == null) {
			act.setActScore(0); // 預設評分為 0
		}
		if (act.getActCnt() == null) {
			act.setActCnt(0); // 預設活動次數/點擊數為 0
		}

		actSvc.addAct(act);

//		// 設置 Flash Attribute，用於 SweetAlert
//		redirectAttributes.addFlashAttribute("successMessage", "新增成功！");
//	
		return "redirect:/fmem/act/listAllActForFmem"; // 要傳URL
	}
}