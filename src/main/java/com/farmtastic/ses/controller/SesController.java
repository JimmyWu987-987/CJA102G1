package com.farmtastic.ses.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ses")
@SessionAttributes({ "sessionSes" })
public class SesController {

	@Autowired
	private ActService actSvc;

	@Autowired
	private SesService sesSvc;

//	================= 單一查詢 (for 場次報名表單用) ==================
	@GetMapping("/register/{actId}/{sesId}")
	public String sesDetail(@PathVariable Integer sesId, ModelMap model) {
		Optional<Ses> optSes = sesSvc.getOneSes(sesId); // 取得場次

		// 防呆用
		if (optSes.isEmpty()) {
			// 查無場次, 導回首頁或活動一覽頁，顯示訊息
			model.addAttribute("message", "查無此場次");
			return "redirect:/act"; // 導回活動一覽頁
		}

		Ses ses = optSes.get();

		// 防呆用, 如果沒上架or是空值(因為活動根本沒過審), 就跳查無此活動or導回首頁
		if (ses.getSesLaunStat() == null || !ses.getSesLaunStat().equals(1)) {
			// 查無場次, 導回首頁或活動一覽頁，顯示訊息
			model.addAttribute("message", "查無此場次");
			return "front_end/customer/unlogined/act/actMainPageTest"; // 回原本的詳細頁
		}

		model.addAttribute("ses", ses);
		model.addAttribute("sessionAct", ses);

		return "front_end/customer/unlogined/ (再看報名表單的連結為何~) "; // 報名表單的部分已另外做了

	}
}