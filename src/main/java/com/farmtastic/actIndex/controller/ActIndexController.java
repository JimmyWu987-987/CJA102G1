package com.farmtastic.actIndex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.actIndex.model.ActIndexService;
import com.farmtastic.actad.model.ActAdService;

@Controller
@RequestMapping("/act")
public class ActIndexController {

    @Autowired 
    private ActAdService actAdService;
    
    @Autowired 
    private ActIndexService actIndexService;
    
    @Autowired 
    private ActService actService;
    
    @GetMapping("")
    public String index(
        Model model
    ) {
    	//活動廣告圖片
        model.addAttribute("adIds", actAdService.getPassActAds());
        
//    	//活動相關資訊(活動名稱,活動敘述,活動開始時間, 活動結束時間, 活動價格) 
//        model.addAttribute("actInfo",actIndexService.getAllForIndex());
        
        List<Act> actList = actService.findByActLaunStat(1, Sort.by(Sort.Direction.DESC, "actLaunUpd"));
        
        model.addAttribute("actInfo", actList);

        if (actList.isEmpty()) {
            model.addAttribute("message", "目前尚無活動");
        }
        
        
        //活動分類
        model.addAttribute("actCate",actIndexService.getAllCateForIndex());

        return "front_end/customer/unlogined/act/act-index";
    }
    
    
    // 活動主圖
    @GetMapping(value = "/img/{id}")
    @ResponseBody
    public byte[] img(@PathVariable Integer id) {
        return actIndexService.getMainImgById(id);
    }
    
    
    // 活動首頁廣告圖片導入活動頁面
    @GetMapping("/ad/{adId}")
    public String goActByAd(@PathVariable Integer adId) {
        Integer actId = actAdService.findActIdByAdId(adId);
        if (actId == null) return "redirect:/act";         
        return "redirect:/act/detail/" + actId;             
    }
    
}
