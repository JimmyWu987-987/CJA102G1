package com.farmtastic.ses.controller;

import java.util.ArrayList;
import java.util.Comparator;
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
import com.farmtastic.act.model.ActCate;
import com.farmtastic.act.model.ActRepository;
import com.farmtastic.act.model.ActService;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesRepository;
import com.farmtastic.ses.model.SesService;

@Controller
@RequestMapping("/fmem/ses")
@SessionAttributes({"sessionFmemSes"})
public class FmemSesController {
	
	@Autowired
	private ActService actSvc;
	
	@Autowired
	private SesService sesSvc;
	
	@Autowired
	private SesService fmemSvc;
	
	@Autowired
    private SesRepository sesRepo;
	
	
	// ========== 查小農自己的全部場次 ==========


//	================= 取得單一場次 >> Act 有了, 改一下即可 ==================



//	================= 新增場次 ==================
//	================= 編輯場次 ==================
//	================= 刪除場次 ==================
}