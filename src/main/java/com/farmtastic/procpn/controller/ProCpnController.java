package com.farmtastic.procpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnVO;

@Controller
@RequestMapping("/procpn")
public class ProCpnController {
	@Autowired
	private ProCpnService proCpnSvc;

	// 查詢全部折價卷
	@GetMapping("listAllProCpn")
	public String listAll(Model model) {
		List<ProCpnVO> list = proCpnSvc.getAll();
		model.addAllAttributes(list);
		return "/back_end/logined/procpn/listAllProCpn";
	}
}
