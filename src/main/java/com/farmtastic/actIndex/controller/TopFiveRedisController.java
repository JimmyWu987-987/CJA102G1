package com.farmtastic.actIndex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.actIndex.model.TopFiveRedisService;
import com.farmtastic.actad.model.ActAdService;

@Controller
@RequestMapping("/hot")
public class TopFiveRedisController {
	
	@Autowired
	private ActService actService;
	
	@Autowired 
    private ActAdService actAdService;
	
    private final TopFiveRedisService hot;

    public TopFiveRedisController(TopFiveRedisService hot) {
        this.hot = hot;
    }

    // 使用者點活動資訊時：+1，再導去詳細頁面
    @GetMapping("/hit/{actId}")
    public String hit(@PathVariable String actId) {
        hot.hit(actId);
        return "redirect:/act/detail/" + actId;  // 轉到詳情
    }
    
    // 使用者點擊活動廣告時 +1
    @GetMapping("/hitByAd/{adId}")
    public String hitByAd(@PathVariable Integer adId,
                          RedirectAttributes redirectAttributes) {

    	Integer actId = actAdService.findActIdByAdId(adId);
        if (actId == null) {
        	redirectAttributes.addFlashAttribute("error", "找不到對應活動");
            return "redirect:/act";
        }

        Act act = actService.getOneAct(actId).orElse(null);
        if (act == null || !Integer.valueOf(1).equals(act.getActLaunStat())) {
        	redirectAttributes.addFlashAttribute("error", "此活動尚未上架");
            return "redirect:/act";
        }

        hot.hit(String.valueOf(actId));
        return "redirect:/act/detail/" + actId;
}
}
