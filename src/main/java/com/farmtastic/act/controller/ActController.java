package com.farmtastic.act.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActCate;
import com.farmtastic.act.model.ActImg;
import com.farmtastic.act.model.ActRepository;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/*
 * ToDo: 修改為 return 的頁面 >> 73. 95. 180
 * */

@Controller
@Validated
@RequestMapping("/act")
@SessionAttributes({ "sessionAct" })
public class ActController {

	@Autowired
	private ActService actSvc;

	@Autowired
	private SesService sesSvc;

	// =========== 消費者查詢活動 ============

	// 查 "已上架的" 所有活動
	@GetMapping("/listAllAct")
	public String listAllActForCus(ModelMap model) {

		// 篩已上架的, 預設依上架更新時間排
		List<Act> actList = actSvc.findByActLaunStat(1, Sort.by(Sort.Direction.DESC, "actLaunUpd"));

		model.addAttribute("actList", actList);

		if (actList.isEmpty()) {
			model.addAttribute("message", "目前尚無活動");
		}

		return "redirect:/act"; // 導回活動一覽頁
	}

	// 複合查詢
	@GetMapping("/listActByCQ")
	public String listActByCQForCus(@RequestParam(required = false) List<Integer> actCateId,
			@RequestParam(required = false) String keyword, ModelMap model) {

		Sort sort = Sort.by(Sort.Direction.DESC, "actLaunUpd");

		List<Act> actList = actSvc.findActByCQForCus(actCateId, keyword, sort);

		model.addAttribute("actList", actList);

		if (actList.isEmpty()) {
			model.addAttribute("message", "查無符合條件的活動");
		}

		return "redirect:/act"; // 導回活動一覽頁
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
		try {
			String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imgBytes));
			if (contentType == null)
				contentType = "image/jpeg"; // 預設 jpeg

			response.setContentType(contentType);
			response.getOutputStream().write(imgBytes);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	// ============ 輪播器圖片 ============
	@GetMapping("/carousel/{actId}")
	@ResponseBody
	public byte[][] getCarouselImages(@PathVariable Integer actId) {
		List<byte[]> imgs = actSvc.getAllActImagesForCarousel(actId);
		return imgs.toArray(new byte[0][]);
	}

	// ============ 單一查詢 (for 活動詳細頁面用) ============
	@GetMapping("/detail/{actId}")
	public String actDetail(@PathVariable Integer actId, ModelMap model) {
		Optional<Act> optAct = actSvc.getOneAct(actId); // 取得活動

		// 防呆用
		if (optAct.isEmpty()) {
			// 查無活動 > 導回首頁或活動一覽頁，顯示訊息
			model.addAttribute("message", "查無此活動");
			return "redirect:/act"; // 導回首頁
		}

		Act act = optAct.get();

		// 如果沒上架or是空值(因為活動根本沒過審), 就跳查無此活動or導回首頁
		if (act.getActLaunStat() == null || !act.getActLaunStat().equals(1)) {
			// 查無活動 > 導回首頁或活動一覽頁，顯示訊息
			model.addAttribute("message", "查無此活動");
			return "redirect:/act"; // 導回首頁
		}

		// 依分類ID排序
		List<ActCate> sortedCate = new ArrayList<>(act.getActCate());
		sortedCate.sort(Comparator.comparing(ActCate::getActCateId));
		model.addAttribute("actCateList", sortedCate);

		// 先依場次日期在依場次時間升冪排序
		Sort sort = Sort.by(Sort.Direction.ASC, "sesDate").and(Sort.by(Sort.Direction.ASC, "sesStart"));

		List<Ses> allSes = sesSvc.findSesByActId(actId, sort);
		List<Ses> launchedSes = allSes.stream().filter(s -> s.getSesLaunStat() != null)
				.filter(s -> s.getSesLaunStat().equals(1)).toList();
		model.addAttribute("sesList", launchedSes);
		model.addAttribute("act", act);
		model.addAttribute("sessionAct", act);

		return "front_end/customer/unlogined/act/actDetails";

	}

}