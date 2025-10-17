package com.farmtastic.act.controller;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.farmtastic.act.model.ActCateRepository;
import com.farmtastic.act.model.ActImg;
import com.farmtastic.act.model.ActRepository;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@Validated
@RequestMapping("/fmem/act")
@SessionAttributes({"sessionFmemAct"})
public class FmemActController {

    @Autowired
    private ActService actSvc;

    @Autowired
    private FmemService fmemSvc;
    
    @Autowired
    private SesService sesSvc;
    
    @Autowired
    private ActRepository actRepo;
    
    @Autowired
    private ActCateRepository actCateRepo;

//	// =========== 編輯活動 ============
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
    
    
    
//	// =========== 上下架活動 ============
    
    
    
//    // =========== 刪除活動 ============
//    @GetMapping("delete/{actId}")
//    public String deleteAct(@PathVariable Integer actId, ModelMap model) {
//        actSvc.deleteAct(actId);
//        model.addAttribute("success", "活動刪除成功！");
//        return "redirect:/act/listAll";
//    }

    
    // =========== 小農查詢自己的活動 ============
    
    // 查全部
    @GetMapping("/listAllActForFmem")		// 之後要登入測試喔喔喔喔喔!!!
    public String listAllActForFmem(HttpSession session, ModelMap model) {
        
    	Fmem fmem = (Fmem) session.getAttribute("loggedInFmember"); // 取得登入小農
        Integer fmemId = fmem.getFmemId();
    	
    	// 塞自己的FmemId
        List<Act> actList = actSvc.findByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "actId"));
        
        model.addAttribute("actList", actList);
        
        if (actList.isEmpty()) {
            model.addAttribute("message", "目前尚無活動");
        }
        
        return "front_end/farmer/logined/fmemAct/listAllActForFmem";
    }
    
    // 查已過審的活動 (上下架用)
    @GetMapping("/launchAct")
    public String listApprovedForLaunch(HttpSession session, ModelMap model) {
    	
    	Fmem fmem = (Fmem) session.getAttribute("sessionFmem"); // 取得登入小農
        Integer fmemId = fmem.getFmemId();
    	
    	List<Act> actList = actSvc.findByFmemIdAndActStat(fmemId, 2, Sort.by(Sort.Direction.DESC, "actLaunUpd"));

    	model.addAttribute("actList", actList);
    	
    	if (actList.isEmpty()) {
            model.addAttribute("message", "目前尚無已過審活動可進行上下架");
        }
    	
    	return "front_end/farmer/logined/fmemAct/listApprovedActForFmem";
    }
    
    
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


//  =========== 新增活動 ============
	@GetMapping("addAct")
	public String showAddActForm(ModelMap model) {
    	model.addAttribute("act", new Act());
    	// 將分類丟給前端使用
    	List<ActCate> allCategories = actCateRepo.findAll();
        model.addAttribute("allCategories", allCategories);
        return "front_end/farmer/logined/fmemAct/addAct";
	}
	
	@PostMapping("insert")
	public String addAct(@Valid @ModelAttribute("act") Act act,
						 BindingResult result,
						 @RequestParam("actMainImg") MultipartFile actMainImg,
						 @RequestParam(value = "actImgs", required = false) MultipartFile[] actImgs,
						 @RequestParam(value = "actCateIds", required = false) List<Integer> actCateIds,
						 HttpSession session,
						 Model model,
						 RedirectAttributes redirectAttributes) throws IOException {

		// 抓登入中的小農
		Fmem fmem = (Fmem) session.getAttribute("sessionFmem"); // 取得登入小農
		Integer fmemId = fmem.getFmemId();
		act.setFmemId(fmemId);

		// 設定狀態&更新時間
		act.setActStat(1);	// 待審核
		act.setActUpd(new Timestamp(System.currentTimeMillis()));
		
		// 不選分類的驗證
		if (actCateIds == null || actCateIds.isEmpty()) {
			result.rejectValue("actCate", null, "請至少選擇一項分類");
		} else {
			Set<ActCate> cates = new HashSet<>();
		    for (Integer id : actCateIds) {
		    	ActCate cate = actCateRepo.findById(id).orElse(null);
		        if (cate != null) cates.add(cate);
		    }
		    act.setActCate(cates);
		}
		
		
		// 開始日期驗證
		java.sql.Date actStart = act.getActStart();
		java.sql.Date after45 = new java.sql.Date(System.currentTimeMillis() + 45L * 24 * 60 * 60 * 1000);
		if (actStart != null && actStart.before(after45)) {
			result.rejectValue("actStart", null, "考慮到審核作業時間及消費者報名時間, 請選擇 45 天之後的日期。");
		}
	        
		// 結束日期驗證
		java.sql.Date actEnd = act.getActEnd();
		if (actStart != null && actEnd != null && actEnd.before(actStart)) {
			result.rejectValue("actEnd", null, "結束日期不得早於開始日期。");
		}

		
		// 主圖驗證
		if (actMainImg == null || actMainImg.isEmpty()) {
			result.rejectValue("actMainImg", null, "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
		} else if (!actMainImg.getContentType().startsWith("image/")) {
			result.rejectValue("actMainImg", null, "只能上傳圖檔");
		} else if (actMainImg.getSize() > 4 * 1024 * 1024) {
			result.rejectValue("actMainImg", null, "活動主要圖片不得超過 4MB");
		} else {
			act.setActMainImg(actMainImg.getBytes());
		}
				
		if (actImgs != null && actImgs.length > 0) {
            // 計算數量
            int count = 0;
            for (MultipartFile file : actImgs) {
                if (!file.isEmpty()) {
                    count++;
                }
            }

            if (count > 5) {
                result.rejectValue("actImgs", null, "最多只能上傳 5 張圖片");
            } else {
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
        }
			
		if (result.hasErrors()) {
			List<ActCate> allCategories = actCateRepo.findAll();
            model.addAttribute("allCategories", allCategories);
			
			return "front_end/farmer/logined/fmemAct/addAct";
		}
		/*************************** 2.開始新增資料 *****************************************/
		actSvc.addAct(act);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			
//		// 設置 Flash Attribute，用於 SweetAlert
//		redirectAttributes.addFlashAttribute("successMessage", "新增成功！");
//	
		return "redirect:/fmem/act/listAllActForFmem";		// 要傳URL
	}
}


//↓ 大吳老師的參考
//  =========== 修改活動 ============
//	@GetMapping("updateAct")
//	public String updateAct(ModelMap model) {
//		Act act = new Act();
//		model.addAttribute("act", act);
//		return "front_end/farmer/logined/fmemAct/addAct";
//	}
//
//	@PostMapping("addAllActImg")
//	public String addActImg(@Valid Act act, BindingResult result, ModelMap model,
//	@RequestParam("upActMainImg") MultipartFile mainImg,
//	@RequestParam("upActImg") MultipartFile[] actImgs)
//	throws IOException {
//		
//		Integer order = 1;
//		
//		// 主圖
//		if (mainImg == null || mainImg.isEmpty()) {
//			model.addAttribute("errorMessage", "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
//			return "front_end/farmer/logined/fmemAct/addAct";
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
//			 return "front_end/farmer/logined/fmemAct/addAct";
//		}
//		
//		/*************************** 2.開始新增資料 *****************************************/
//		actSvc.addAct(act);
//		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//		List<Act> list = actSvc.getAllAct();
//			model.addAttribute("actListData", list);
//			model.addAttribute("success", "- (新增成功)");
//			return "redirect:front_end/farmer/logined/fmemAct/listAllAct";
//	}
//	
//	
//	
//	@PostMapping("getOne_For_Display")
//	public String getOne_For_Display(
//		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//		@NotEmpty(message="員工編號: 請勿空白")
//		@Digits(integer = 4, fraction = 0, message = "員工編號: 請填數字-請勿超過{integer}位數")
//		@Min(value = 7001, message = "員工編號: 不能小於{value}")
//		@Max(value = 7777, message = "員工編號: 不能超過{value}")
//		@RequestParam("empno") String empno,
//		ModelMap model) {
//		
//		/***************************2.開始查詢資料*********************************************/
////		EmpService empSvc = new EmpService();
//		EmpVO empVO = empSvc.getOneEmp(Integer.valueOf(empno));
//		
//		List<EmpVO> list = empSvc.getAll();
//		model.addAttribute("empListData", list);     // for select_page.html 第97 109行用
//		model.addAttribute("deptVO", new DeptVO());  // for select_page.html 第133行用
//		List<DeptVO> list2 = deptSvc.getAll();
//    	model.addAttribute("deptListData",list2);    // for select_page.html 第135行用
//		
//		if (empVO == null) {
//			model.addAttribute("errorMessage", "查無資料");
//			return "back-end/emp/select_page";
//		}
//		
//		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
//		model.addAttribute("empVO", empVO); // for1 --> listOneEmp.html 的第37~44行用
//                                            // for2 --> select_page.html的第156用
////		return "back-end/emp/listOneEmp";   // 查詢完成後轉交listOneEmp.html
//		return "back-end/emp/select_page";  // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
//	}
//
//	
//	@ExceptionHandler(value = { ConstraintViolationException.class })
//	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
//	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//	    StringBuilder strBuilder = new StringBuilder();
//	    for (ConstraintViolation<?> violation : violations ) {
//	          strBuilder.append(violation.getMessage() + "<br>");
//	    }
//	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
////	    model.addAttribute("empVO", new EmpVO());
////    	EmpService empSvc = new EmpService();
//		List<EmpVO> list = empSvc.getAll();
//		model.addAttribute("empListData", list);     // for select_page.html 第97 109行用
//		model.addAttribute("deptVO", new DeptVO());  // for select_page.html 第133行用
//		List<DeptVO> list2 = deptSvc.getAll();
//    	model.addAttribute("deptListData",list2);    // for select_page.html 第135行用
//		String message = strBuilder.toString();
//	    return new ModelAndView("back-end/emp/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
//	}
	
	
	
	
//	// 去除BindingResult中某個欄位的FieldError紀錄
//	public BindingResult removeFieldError(Act act, BindingResult result, String removedFieldname) {
//		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
//				.collect(Collectors.toList());
//		result = new BeanPropertyBindingResult(act, "act");
//		for (FieldError fieldError : errorsListToKeep) {
//			result.addError(fieldError);
//		}
//		return result;
//	}
//	
//}