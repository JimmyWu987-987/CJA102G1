package com.farmtastic.act.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

@Controller
@Validated
@RequestMapping("/admin/act")
@SessionAttributes({"sessionAdminAct"})
public class AdminActController {

    @Autowired
    private ActService actSvc;

    @Autowired
    private FmemService fmemSvc;
    
    @Autowired
    private SesService sesSvc;
    
    @Autowired
    private ActRepository actRepo;

//	// =========== 審核活動 ==============
    
    // 先抓未審核的活動
    @GetMapping("listUnapprovedAct")
    public String listUnapprovedAct(Model model) {        
    	
    	// 預設依狀態更新時間排
        List<Act> actList = actSvc.findByActStat(1, Sort.by(Sort.Direction.DESC, "actUpd"));
        
        model.addAttribute("actList", actList);

        if (actList.isEmpty()) {
            model.addAttribute("message", "目前尚無活動");
        }
        return "back_end/logined/backAct/reviewPage";		// 回審核首頁
    }
    
    // 進行審核
    @PostMapping("reviewAct")
    public String reviewAct(@RequestParam("actId") Integer actId,
    						@RequestParam("action") String action,   // 過審OR不過審
            				@RequestParam(value = "actRemark", required = false) String actRemark,
            				RedirectAttributes redirectAttributes,
            				Model model) {
    	
    	// 防呆用
    	Optional<Act> optAct = actSvc.getOneAct(actId);
    	if (optAct.isEmpty()) {
        	redirectAttributes.addFlashAttribute("errorMessage", "找不到該活動資料");
            return "redirect:/backAct/listUnapprovedAct";
        }
        
        Act act = optAct.get();

        // 過審
        if ("approve".equals(action)) {
        	act.setActStat(2);
        	act.setActRemark(null);
        } else if ("reject".equals(action)) {
        	if (actRemark == null || actRemark.trim().isEmpty()) {
        		// 不通過但沒寫原因 → 回詳細頁
        		model.addAttribute("errorMessage", "若不通過, 請輸入未通過原因");
        		model.addAttribute("act", act);
        		return "back_end/logined/backAct/reviewDetail"; // 不要加 ?actId
        	}
        	act.setActStat(3);
        	act.setActRemark(actRemark);
        }  else {
        	redirectAttributes.addFlashAttribute("errorMessage", "無效的操作");
        	return "redirect:/backAct/listUnapprovedAct";
        }

        act.setActUpd(new Timestamp(System.currentTimeMillis()));

        actSvc.reviewAct(act);

        redirectAttributes.addFlashAttribute("successMessage", "已完成審核");
        return "redirect:/backAct/listUnapprovedAct";
    }

//		
//		// 主圖
//		if (mainImg == null || mainImg.isEmpty()) {
//			model.addAttribute("errorMessage", "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
//			return "front_end/farmer/logined/fmemAct/addEmp";
//		}
//		
//		act.setActMainImg(mainImg.getBytes());
//		
//		// 活動圖片 (可有可無) 
//		if (actImgs != null) {
//			for (MultipartFile file : actImgs) {
//				if (! file.isEmpty()) {
//					ActImg actImg = new ActImg();
//					actImg.setActImg(file.getBytes());
//					actImg.setActimgOrder(order);
//					order++;
//				}
//			}
//		}
//		
//		if (result.hasErrors()) {
//			 return "front_end/farmer/logined/fmemAct/addEmp";
//		}
//		
//		/*************************** 2.開始新增資料 *****************************************/
//		actSvc.addAct(act);
//		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//		List<Act> list = actSvc.getAllAct();
//			model.addAttribute("actListData", list);
//			return "redirect:back_end/logined/backAct/reviewPage";
//    	
//    }
     
 // =========== 查詢活動 ==============
    // 所有活動
    @GetMapping("/listAllAct")
    public String listActForAdmin(ModelMap model) {
    	
    	// 預設活動ID升冪
    	Sort sort = Sort.by(Sort.Direction.ASC, "actId");
    	List<Act> actList = actSvc.getAllAct(sort);
        
        model.addAttribute("actList", actList);

        if (actList.isEmpty()) {
        	model.addAttribute("message", "目前尚無活動");
        }
        
        return "back_end/logined/backAct/listAllActForAdmin";		// 導回首頁
    }
    
    
    // 複合查詢
    @GetMapping("/findByCQ")
    public String listActByCQForAdmin(@RequestParam(required = false) List<Integer> actCateId,
    								  @RequestParam(required = false) String keyword,
    								  ModelMap model) {
    	
    	// 依活動狀態更新時間降冪
    	Sort sort = Sort.by(Sort.Direction.DESC, "actUpd");
        
        List<Act> actList = actSvc.findActByCQForCus(actCateId, keyword, sort);
        
        model.addAttribute("actList", actList);

        if (actList.isEmpty()) {
        	model.addAttribute("message", "查無符合條件的活動");
        }
        
        return "back_end/logined/backAct/selectPage";		// 導到查詢結果頁
    }
    
    
    
//	@GetMapping("updateAct/{actId}")
//	public String getUpdatePage(@PathVariable Integer actId, ModelMap model) {
//		Act act = actSvc.getOneAct(actId);
//		model.addAttribute("act", act);
//		return "front_end/farmer/logined/fmemAct/updateAct";
//	}
//
//	@PostMapping("update")
//	public String updateAct(@ModelAttribute("act") Act act, ModelMap model) {
//		actSvc.updateAct(act);
//		model.addAttribute("success", "活動修改成功！");
//		return "redirect:/act/listAll";
//	}
//
//	// =========== 查詢單筆活動 ============
//	@GetMapping("getOne/{actId}")
//	public String getOneAct(@PathVariable Integer actId, ModelMap model) {
//		Act act = actSvc.getOneAct(actId);
//		model.addAttribute("act", act);
//		return "front_end/farmer/logined/fmemAct/oneAct";
//	}

//	// =========== 小農查詢多個活動 ============
//    
//    // 查全部
//    @GetMapping("/listAllActForFmem/{fmemId}")
//    public String listAllByFmem(@PathVariable Integer fmemId, ModelMap model) {
//        
//        List<Act> actList = actSvc.findByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "actId"));
//        model.addAttribute("actList", actList);
//        return "front_end/farmer/logined/fmemAct/listAllActForFmem";
//    }
//    
//    // 查已過審的活動 (上下架用)
//    @GetMapping("/listApprovedActForFmem/{fmemId}")
//    public String listApprovedForLaunch(@PathVariable Integer fmemId, ModelMap model) {
//    	List<Act> actList = actRepo.findAll(Sort.by(Sort.Direction.DESC, "actLaunUpd"));
//
//    	List<Act> filtered = new ArrayList<>();
//    	for (Act act : actList) {
//    		if (act.getFmemId().equals(fmemId) && act.getActStat().equals(2)) {
//    			filtered.add(act);
//    		}
//    	}
//
//    	model.addAttribute("actList", filtered);
//    	return "front_end/farmer/logined/fmemAct/listApprovedActForFmem";
//    }
    
    
	// =========== 抓圖 ============

    // get主圖
    @GetMapping("/mainImg/{actId}")
    public void getActMainImg(@PathVariable Integer actId, HttpServletResponse response) {
    	Optional<Act> optAct = actSvc.getOneAct(actId);		// 取得活動
    	
    	if (optAct.isEmpty()) {
    	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    	    return;
    	}
    	
    	byte[] actMainImg;
    	
    	Act act = optAct.get();
    	
    	if (act.getActMainImg() == null) {
    		actMainImg = new byte[0];  // 空白圖
    	}	else {
    		actMainImg = optAct.get().getActMainImg();
    	}

        writeImageToResponse(actMainImg, response);
    }

    // get其他活動圖
    @GetMapping("/img/{actId}/{imgId}")
    public void getActImg(@PathVariable Integer actId,
                          @PathVariable Integer imgId,
                          HttpServletResponse response) {
    	Optional<Act> optAct = actSvc.getOneAct(actId);		// 取得活動

    	if (optAct.isEmpty()) {
    	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    	    return;
    	}
    	
    	Act act = optAct.get();

        if (act.getActImg() == null || act.getActImg().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        ActImg img = act.getActImg().stream()
        							.filter(i -> i.getActImgId().equals(imgId))
        							.findFirst()
        							.orElse(null);

        if (img == null || img.getActImg() == null || img.getActImg().length == 0) {
        	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        	return;
        }

        writeImageToResponse(img.getActImg(), response);
	}

	private void writeImageToResponse(byte[] imgBytes, HttpServletResponse response) {
		try {
			String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imgBytes));
			if (contentType == null) contentType = "image/jpeg"; // 預設 jpeg

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
    
    // ============ 活動詳細頁 (for 後台) ============
    @GetMapping("/reviewDetail/{actId}")
    public String actDetail(@PathVariable Integer actId, ModelMap model) {
    	Optional<Act> optAct = actSvc.getOneAct(actId);		// 取得活動
    	
    	// 防呆用
    	if (optAct.isEmpty()) {
            // 查無活動 > 跑去"查無此活動"頁面
            model.addAttribute("message", "查無此活動");
            return "back_end/logined/noAct"; 		// 做一個 "查無此活動"頁面
        }
    	
    	Act act = optAct.get();
    	

        // 依分類ID排序
        List<ActCate> sortedCate = new ArrayList<>(act.getActCate());
        sortedCate.sort(Comparator.comparing(ActCate::getActCateId));
        model.addAttribute("actCateList", sortedCate);

        model.addAttribute("act", act);
        model.addAttribute("sessionAct", act);
        
        return "back_end/logined/backAct/reviewDetails";
        
    }

}