//package com.farmtastic.act.controller;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
// 
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.farmtastic.act.model.Act;
//import com.farmtastic.act.model.ActCate;
//import com.farmtastic.act.model.ActImg;
//import com.farmtastic.act.model.ActRepository;
//import com.farmtastic.act.model.ActService;
//import com.farmtastic.fmember.model.FmemService;
//import com.farmtastic.ses.model.Ses;
//import com.farmtastic.ses.model.SesService;
//
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//
//@Controller
//@Validated
//@RequestMapping("/admin/act")
//@SessionAttributes({"sessionAdminAct"})
//public class FmemActController {
//
//    @Autowired
//    private ActService actSvc;
//
//    @Autowired
//    private FmemService fmemSvc;
//    
//    @Autowired
//    private SesService sesSvc;
//    
//    @Autowired
//    private ActRepository actRepo;
//
//    // =========== 新增活動 ============
////	@GetMapping("addAct")
////	public String addAct(ModelMap model) {
////		Act act = new Act();
////		model.addAttribute("act", act);
////		return "front_end/farmer/logined/fmemAct/addAct";
////	}
////	
////	@PostMapping("addAllActImg")
////	public String addActImg(@Valid Act act, BindingResult result, ModelMap model,
////	@RequestParam("upActMainImg") MultipartFile mainImg,
////	@RequestParam("upActImg") MultipartFile[] actImgs)
////	throws IOException {
////		
////		Integer order = 1;
////		
////		// 主圖
////		if (mainImg == null || mainImg.isEmpty()) {
////			model.addAttribute("errorMessage", "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
////			return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		act.setActMainImg(mainImg.getBytes());
////		
////		// 活動圖片 (可有可無) 
////		if (actImgs != null) {
////			for (MultipartFile file : actImgs) {
////				if (! file.isEmpty()) {
////					ActImg actImg = new ActImg();
////					actImg.setActImg(file.getBytes());
////					actImg.setActimgOrder(order);
////					order++;
////				}
////			}
////		}
////		
////		if (result.hasErrors()) {
////			 return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		/*************************** 2.開始新增資料 *****************************************/
////		actSvc.addAct(act);
////		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
////		List<Act> list = actSvc.getAllAct();
////			model.addAttribute("actListData", list);
////			model.addAttribute("success", "- (新增成功)");
////			return "redirect:front_end/farmer/logined/fmemAct/listAllAct";
////	}
//    
////
////	@PostMapping("insert")
////	public String insertAct(@ModelAttribute("act") Act act, ModelMap model) {
////		actSvc.addAct(act);
////		model.addAttribute("success", "活動新增成功！");
////		return "redirect:/act/listAll";
////	}
////
////	// =========== 修改活動 ============
////	@GetMapping("updateAct/{actId}")
////	public String getUpdatePage(@PathVariable Integer actId, ModelMap model) {
////		Act act = actSvc.getOneAct(actId);
////		model.addAttribute("act", act);
////		return "front_end/farmer/logined/fmemAct/updateAct";
////	}
////
////	@PostMapping("update")
////	public String updateAct(@ModelAttribute("act") Act act, ModelMap model) {
////		actSvc.updateAct(act);
////		model.addAttribute("success", "活動修改成功！");
////		return "redirect:/act/listAll";
////	}
////
////	// =========== 查詢單筆活動 ============
////	@GetMapping("getOne/{actId}")
////	public String getOneAct(@PathVariable Integer actId, ModelMap model) {
////		Act act = actSvc.getOneAct(actId);
////		model.addAttribute("act", act);
////		return "front_end/farmer/logined/fmemAct/oneAct";
////	}
//
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
//    
//    
//	// =========== 抓圖 ============
//
//	// get抓主圖
//    @GetMapping("/mainImg/{actId}")
//    public void getActMainImg(@PathVariable Integer actId, HttpServletResponse response) {
//        Act act = actSvc.getOneAct(actId);
//        byte[] actMainImg = act.getActMainImg();
//        writeImageToResponse(actMainImg, response);
//    }
//
//    // get其他活動圖
//    @GetMapping("/img/{actId}/{imgId}")
//    public void getActImg(@PathVariable Integer actId,
//                          @PathVariable Integer imgId,
//                          HttpServletResponse response) {
//        Act act = actSvc.getOneAct(actId);
//
//        if (act.getActImg() == null || act.getActImg().isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            return;
//        }
//
//        ActImg img = act.getActImg().stream()
//                         .filter(i -> i.getActImgId().equals(imgId))
//                         .findFirst()
//                         .orElse(null);
//
//        if (img == null || img.getActImg() == null || img.getActImg().length == 0) {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            return;
//        }
//
//        writeImageToResponse(img.getActImg(), response);
//    }
//
//    private void writeImageToResponse(byte[] imgBytes, HttpServletResponse response) {
//        try {
//            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imgBytes));
//            if (contentType == null) contentType = "image/jpeg"; // 預設 jpeg
//
//            response.setContentType(contentType);
//            response.getOutputStream().write(imgBytes);
//            response.getOutputStream().flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//    }
//    
//    
//    // ============ 活動詳細頁 (for 小農) ============
//    @GetMapping("/detail/{actId}")
//    public String actDetail(@PathVariable Integer actId, ModelMap model) {
//        Act act = actSvc.getOneAct(actId);		// 取得活動
//        
//        
//        // 依分類ID排序
//        List<ActCate> sortedCate = new ArrayList<>(act.getActCate());
//        sortedCate.sort(Comparator.comparing(ActCate::getActCateId));
//        model.addAttribute("actCateList", sortedCate);
//
//        // 先依場次日期再依場次時間升冪排序
//        Sort sort = Sort.by(Sort.Direction.ASC, "sesDate")
//        				.and(Sort.by(Sort.Direction.ASC, "sesStart"));
//        
//        List<Ses> allSes = sesSvc.findByActId(actId, sort);
//        List<Ses> launchedSes = allSes.stream()
//            .filter(s -> s.getSesLaunStat() != null && s.getSesLaunStat().equals(1))
//            .toList();
//        model.addAttribute("sesList", launchedSes);
//        model.addAttribute("act", act);
//        model.addAttribute("sessionFmemAct", act);
//        
//        return "front_end/customer/unlogined/actDetails/actDetailsForFmem";
//        
//    }
//    
//    // ============ 輪播器圖片 ============
//    @GetMapping("/carousel/{actId}")
//    @ResponseBody
//    public byte[][] getCarouselImages(@PathVariable Integer actId) {
//        List<byte[]> imgs = actSvc.getAllActImagesForCarousel(actId);
//        return imgs.toArray(new byte[0][]);
//    }
//    
//    
//
//    
////    // =========== 刪除活動 ============
////    @GetMapping("delete/{actId}")
////    public String deleteAct(@PathVariable Integer actId, ModelMap model) {
////        actSvc.deleteAct(actId);
////        model.addAttribute("success", "活動刪除成功！");
////        return "redirect:/act/listAll";
////    }
//    
//    
//}
//
//
//
//
//
//////  =========== 新增活動 ============
////	@GetMapping("addAct")
////	public String addAct(ModelMap model) {
////		Act act = new Act();
////		model.addAttribute("act", act);
////		return "front_end/farmer/logined/fmemAct/addEmp";
////	}
////
////	@PostMapping("addAllActImg")
////	public String addActImg(@Valid Act act, BindingResult result, ModelMap model,
////	@RequestParam("upActMainImg") MultipartFile mainImg,
////	@RequestParam("upActImg") MultipartFile[] actImgs)
////	throws IOException {
////		
////		Integer order = 1;
////		
////		// 主圖
////		if (mainImg == null || mainImg.isEmpty()) {
////			model.addAttribute("errorMessage", "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
////			return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		act.setActMainImg(mainImg.getBytes());
////		
////		// 活動圖片 (可有可無) 
////		if (actImgs != null) {
////			for (MultipartFile file : actImgs) {
////				if (! file.isEmpty()) {
////					ActImg actImg = new ActImg();
////					actImg.setActImg(file.getBytes());
////					actImg.setActimgOrder(order);
////					order++;
////				}
////			}
////		}
////		
////		if (result.hasErrors()) {
////			 return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		/*************************** 2.開始新增資料 *****************************************/
////		actSvc.addAct(act);
////		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
////		List<Act> list = actSvc.getAllAct();
////			model.addAttribute("actListData", list);
////			model.addAttribute("success", "- (新增成功)");
////			return "redirect:front_end/farmer/logined/fmemAct/listAllAct";
////	}
////	
//////  =========== 修改活動 ============
////	@GetMapping("updateAct")
////	public String updateAct(ModelMap model) {
////		Act act = new Act();
////		model.addAttribute("act", act);
////		return "front_end/farmer/logined/fmemAct/addEmp";
////	}
////
////	@PostMapping("addAllActImg")
////	public String addActImg(@Valid Act act, BindingResult result, ModelMap model,
////	@RequestParam("upActMainImg") MultipartFile mainImg,
////	@RequestParam("upActImg") MultipartFile[] actImgs)
////	throws IOException {
////		
////		Integer order = 1;
////		
////		// 主圖
////		if (mainImg == null || mainImg.isEmpty()) {
////			model.addAttribute("errorMessage", "請上傳活動首圖(將顯示於活動一覽頁面及活動詳情中)");
////			return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		act.setActMainImg(mainImg.getBytes());
////		
////		// 活動圖片 (可有可無) 
////		if (actImgs != null) {
////			for (MultipartFile file : actImgs) {
////				if (! file.isEmpty()) {
////					ActImg actImg = new ActImg();
////					actImg.setActImg(file.getBytes());
////					actImg.setActimgOrder(order);
////					order++;
////				}
////			}
////		}
////		
////		if (result.hasErrors()) {
////			 return "front_end/farmer/logined/fmemAct/addEmp";
////		}
////		
////		/*************************** 2.開始新增資料 *****************************************/
////		actSvc.addAct(act);
////		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
////		List<Act> list = actSvc.getAllAct();
////			model.addAttribute("actListData", list);
////			model.addAttribute("success", "- (新增成功)");
////			return "redirect:front_end/farmer/logined/fmemAct/listAllAct";
////	}
////	
////	
////	
//	
//	
////
////	/*
////	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
////	 */
////	@PostMapping("insert")
////	public String insert(@Valid EmpVO empVO, BindingResult result, ModelMap model,
////			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {
////
////		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
////		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
////		result = removeFieldError(empVO, result, "upFiles");
////
////		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
////			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
////		} else {
////			for (MultipartFile multipartFile : parts) {
////				byte[] buf = multipartFile.getBytes();
////				empVO.setUpFiles(buf);
////			}
////		}
////		if (result.hasErrors() || parts[0].isEmpty()) {
////			return "back-end/emp/addEmp";
////		}
////		/*************************** 2.開始新增資料 *****************************************/
////		// EmpService empSvc = new EmpService();
////		empSvc.addEmp(empVO);
////		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
////		List<EmpVO> list = empSvc.getAll();
////		model.addAttribute("empListData", list); // for listAllEmp.html 第85行用
////		model.addAttribute("success", "- (新增成功)");
////		return "redirect:/emp/listAllEmp"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
////	}
//	
//	
//	
//	
//	
//	
//	
//	
////  =========== 新增或修改活動 ============
////  public List<String> saveOrUpdateAct(Act act) {
////      List<String> errorMsgs = new ArrayList<>();        
////
////      java.sql.Date after45 = new java.sql.Date(System.currentTimeMillis() + 45L * 24 * 60 * 60 * 1000);
////      
////      if (act.getActStart().before(after45)){
////          errorMsgs.add("由於審核需要作業時間, 請輸入 45 天之後的日期。");
////      }
////      
////      if (act.getActEnd().before(act.getActStart())) {
////          errorMsgs.add("結束日期不得早於開始日期。");
////      }
////      act.setFmemId(act.getFmemId());
////
////      // 新增or修改並送審後, 狀態會變為1(待審核)
////   	Integer actStat = 1;
////   	act.setActStat(actStat);
////
////   	act.setActUpd(new Timestamp(System.currentTimeMillis()));
////
////      if (!errorMsgs.isEmpty()) {
////          return errorMsgs;
////      }
////
////      actRepository.save(act);
////      
////      return errorMsgs;
////  }
//	
//	
//	
//	
//
//
////	@PostMapping("getOne_For_Display")
////	public String getOne_For_Display(
////		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
////		@NotEmpty(message="員工編號: 請勿空白")
////		@Digits(integer = 4, fraction = 0, message = "員工編號: 請填數字-請勿超過{integer}位數")
////		@Min(value = 7001, message = "員工編號: 不能小於{value}")
////		@Max(value = 7777, message = "員工編號: 不能超過{value}")
////		@RequestParam("empno") String empno,
////		ModelMap model) {
////		
////		/***************************2.開始查詢資料*********************************************/
//////		EmpService empSvc = new EmpService();
////		EmpVO empVO = empSvc.getOneEmp(Integer.valueOf(empno));
////		
////		List<EmpVO> list = empSvc.getAll();
////		model.addAttribute("empListData", list);     // for select_page.html 第97 109行用
////		model.addAttribute("deptVO", new DeptVO());  // for select_page.html 第133行用
////		List<DeptVO> list2 = deptSvc.getAll();
////    	model.addAttribute("deptListData",list2);    // for select_page.html 第135行用
////		
////		if (empVO == null) {
////			model.addAttribute("errorMessage", "查無資料");
////			return "back-end/emp/select_page";
////		}
////		
////		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
////		model.addAttribute("empVO", empVO); // for1 --> listOneEmp.html 的第37~44行用
////                                            // for2 --> select_page.html的第156用
//////		return "back-end/emp/listOneEmp";   // 查詢完成後轉交listOneEmp.html
////		return "back-end/emp/select_page";  // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
////	}
////
////	
////	@ExceptionHandler(value = { ConstraintViolationException.class })
////	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
////	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
////	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
////	    StringBuilder strBuilder = new StringBuilder();
////	    for (ConstraintViolation<?> violation : violations ) {
////	          strBuilder.append(violation.getMessage() + "<br>");
////	    }
////	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
//////	    model.addAttribute("empVO", new EmpVO());
//////    	EmpService empSvc = new EmpService();
////		List<EmpVO> list = empSvc.getAll();
////		model.addAttribute("empListData", list);     // for select_page.html 第97 109行用
////		model.addAttribute("deptVO", new DeptVO());  // for select_page.html 第133行用
////		List<DeptVO> list2 = deptSvc.getAll();
////    	model.addAttribute("deptListData",list2);    // for select_page.html 第135行用
////		String message = strBuilder.toString();
////	    return new ModelAndView("back-end/emp/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
////	}
//	
//	
//	
//	
////	// 去除BindingResult中某個欄位的FieldError紀錄
////	public BindingResult removeFieldError(Act act, BindingResult result, String removedFieldname) {
////		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
////				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
////				.collect(Collectors.toList());
////		result = new BeanPropertyBindingResult(act, "act");
////		for (FieldError fieldError : errorsListToKeep) {
////			result.addError(fieldError);
////		}
////		return result;
////	}
////	
////}