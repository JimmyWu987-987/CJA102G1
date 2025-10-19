package com.farmtastic.ses.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;


import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/fmem/ses")
@SessionAttributes({"sessionFmemSes"})
public class FmemSesController {
	
	@Autowired
	private SesService sesSvc;
	
	// TODO: 新增場次
	
	// ========== 查小農自己的全部場次 ==========
	// 查全部
    @GetMapping("/listAllSesForFmem")		// 之後要登入測試喔喔喔喔喔!!!
    public String listAllSesForFmem(HttpSession session, ModelMap model) {

    	Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");			// 取得登入小農
    	if (fmem == null) {
    		// 沒登入的防呆
    		model.addAttribute("message", "請先登入小農頁面, 謝謝");
    		// 導回小農登入頁
    		return "redirect:/showFmemRegLoginForm";
    	}
        Integer fmemId = fmem.getFmemId();
    	
    	// 塞自己的FmemId、依日期排序
        List<Ses> sesList = sesSvc.findSesWithActByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "sesDate"));
        
        model.addAttribute("sesList", sesList);
        
        if (sesList.isEmpty()) {
            model.addAttribute("message", "您尚未創建場次");
            return "front_end/farmer/logined/fmemSes/listAllSesForFmem"; 		// 沒有場次就回場次一覽
        }
        
        return "front_end/farmer/logined/fmemSes/listAllSesForFmem";
    }


	//	================= 上下架場次 ==================
    @PostMapping("/toggleLaunchStat")
    public String toggleLaunchStat(@RequestParam("sesId") Integer sesId, 
                                   @RequestParam("targetStat") Integer targetStat, // 1:上架, 0:下架
                                   HttpSession session, 
                                   ModelMap model) {
        
         Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); 
         
         // 登入檢查 (避免 500 錯誤)
         if (fmem == null) {
             return "redirect:/showFmemRegLoginForm";
         }
         Integer fmemId = fmem.getFmemId();
        
         Optional<Ses> sesOpt = sesSvc.getOneSes(sesId);
        
         if (sesOpt.isPresent()) {
             Ses ses = sesOpt.get();
             
             // 權限檢查
             if (ses.getAct() == null || !ses.getAct().getFmem().getFmemId().equals(fmemId)) {
                 model.addAttribute("errorMessage", "無權限操作此場次");
                 return "redirect:/fmem/ses/listAllSesForFmem"; 
             }
             
             // 執行上下架操作：將 sesLaunStat 設為目標狀態
             ses.setSesLaunStat(targetStat); 
             ses.setSesLaunUpd(new Timestamp(System.currentTimeMillis()));
             
             String message;
             if (targetStat == 0) {
                 ses.setRegStat(0); 
                 message = "場次 ID " + sesId + " 已完成下架";
             } else {
                 // 1 (上架)：同時設定報名狀態為 1 (報名中)
                 ses.setRegStat(1);
                 message = "場次 ID " + sesId + " 已完成上架";
             }
             
             sesSvc.updateSes(ses, fmemId); 
             model.addAttribute("successMessage", message);
                
         } else {
             model.addAttribute("errorMessage", "找不到該場次 ID: " + sesId);
         }
        
         return "redirect:/fmem/ses/listAllSesForFmem"; 
    }    
    
	//	================= 取消場次 (= 編輯報名狀態+下架) ==================
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