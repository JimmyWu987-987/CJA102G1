//package com.farmtastic.ses.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
//
//import com.farmtastic.act.model.Act;
//import com.farmtastic.act.model.ActRepository;
//import com.farmtastic.act.model.ActService;
//import com.farmtastic.ses.model.Ses;
//import com.farmtastic.ses.model.SesRepository;
//import com.farmtastic.ses.model.SesService;
//
//@Controller
//@RequestMapping("/fmem/ses")
//@SessionAttributes({"sessionFmemSes"})
//public class FmemSesController {
//	
//	@Autowired
//	private ActService actSvc;
//	
//	@Autowired
//	private SesService sesSvc;
//	
//	@Autowired
//    private SesRepository sesRepo;
//
////	================= 列出該活動所有場次 ================
//	@GetMapping("/listByAct/{actId}")
//	public String listByAct(@PathVariable("actId") Integer actId, ModelMap model) {
//		Sort sort = Sort.by(Sort.Direction.ASC, "sesDate");
//
//		Act act = actSvc.getOneAct(actId);
//		
//		// 依活動ID查所有場次
//		List<Ses> sesList = sesSvc.findByActId(actId, sort);
//
//		model.addAttribute("act", act);
//		model.addAttribute("sesList", sesList);
//		model.addAttribute("actId", actId);
//
//		return "front_end/customer/unlogined/actDetails/actDetails"; 
//
//	}
//	
//	
////	================= 取得單一場次 ==================
//	
//	
////	        model.addAttribute("sessionFmemSes", ses);	
//	
//	
//	
//	
//	
//}