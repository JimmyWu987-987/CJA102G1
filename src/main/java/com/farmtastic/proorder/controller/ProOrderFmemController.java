package com.farmtastic.proorder.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.proorder.model.FmemOrderSummary;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/fmem/proorders")
public class ProOrderFmemController {

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");

		// 登入驗證交給 Fitter 攔截

		// 取得該小農的所有客戶訂單
		List<FmemOrderSummary> list = proOrdSvc.getAllByFmemId(loggedInFmember.getFmemId());

		model.addAttribute("proOrderList", list);

		return "/front_end/farmer/logined/fmemProOrders/listAllProOrder";

	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		// 查詢DB內的訂單以及訂單明細表格
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		List<ProOrderItemVO> items = ProOrderItemSvc.getProOrderItems(proOrderVO);
		// 放入 model 傳送至前端
		model.addAttribute("proOrderVO", proOrderVO);
		model.addAttribute("proOrderItems", items);

		// 給前端設定下拉式選單的屬性
		setColumsStatus(model);

		return "/front_end/farmer/logined/fmemProOrders/listOneProOrder";
	}

	// 小農會員修改訂各欄位的狀態
	@PostMapping("updateStatus")
	public String update(@RequestParam("proOrdId") Integer proOrdId, @RequestParam("proOrdStatus") Byte proOrdStatus,
			@RequestParam("proTrackingNo") String proTrackingNo,
			@RequestParam(value = "proOrdShipdate", required = false) String proOrdShipdate, ModelMap model) {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(proOrdId);

		boolean error = false;

		if (proOrderVO.getProOrdStatus() == proOrdStatus) {
			System.err.println("沒有更新資料！");
			model.addAttribute("errorMessage", "沒有更新資料！");

			error = true;

		}

		if (proTrackingNo == null || proTrackingNo.trim().isEmpty()) {
			System.err.println("請輸入物流追蹤碼！");
			model.addAttribute("errorMessage", "請輸入物流追蹤碼！");
			error = true;
		} else {
			proOrderVO.setProTrackingNo(proTrackingNo);
		}

		// **修正日期處理邏輯：將字串轉換為日期物件**
		if (proOrdShipdate != null && !proOrdShipdate.isEmpty()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date shipDate = sdf.parse(proOrdShipdate);
				// 假設您的 VO 屬性是 java.util.Date 或其子類
				proOrderVO.setProOrdShipdate(shipDate);

			} catch (ParseException e) {
				// 處理日期格式錯誤，例如：記錄錯誤或加入 Model 讓前端顯示
				System.err.println("日期格式錯誤: " + e.getMessage());
				model.addAttribute("errorMessage", "日期格式錯誤");
			}
		} else {
			System.err.println("沒有輸入日期資料！");
			model.addAttribute("errorMessage", "沒有輸入日期資料！");
			error = true;
		}

		if (proOrdStatus == 0) {
			System.err.println("沒有更改訂單狀態！");
			model.addAttribute("errorMessage", "沒有更改訂單狀態！");
			error = true;
		}

		if (error) {
			setColumsStatus(model);
			model.addAttribute("proOrderVO", proOrderVO);
			model.addAttribute("proOrderItems", proOrderVO.getProOrderItems());
			return "/front_end/farmer/logined/fmemProOrders/listOneProOrder";
		} else {

			proOrderVO.setProOrdStatus(proOrdStatus);
			proOrderVO.setProTrackingNo(proTrackingNo);

			proOrdSvc.updateProOrder(proOrderVO);
			model.addAttribute("successMessage", "訂單狀態更新成功！");

			// 放入 model 傳送至前端
			model.addAttribute("proOrderVO", proOrderVO);
			model.addAttribute("proOrderItems", proOrderVO.getProOrderItems());
			// 給前端設定下拉式選單的屬性
			setColumsStatus(model);

			return "/front_end/farmer/logined/fmemProOrders/listOneProOrder";
		}
	}

	// 給前端設定下拉式選單的屬性
	public void setColumsStatus(ModelMap model) {
//		========================
		// 設定訂單狀態，給前端做下拉式選單
		Map<Integer, String> proOrdStatus = new HashMap<>();
		proOrdStatus.put(0, "成立訂單");
		proOrdStatus.put(1, "取消訂單(未出貨)");
		proOrdStatus.put(2, "出貨中");
		proOrdStatus.put(3, "已到貨");
		proOrdStatus.put(4, "申請退貨(已出貨)");
		proOrdStatus.put(5, "退貨中");
		proOrdStatus.put(6, "已退貨");
//		proOrdStatus.put(7, "貨物已燒毀");
//		proOrdStatus.put(8, "貨物已沉入大海");
//		proOrdStatus.put(9, "貨物被偷了");
//		proOrdStatus.put(10, "貨物已失蹤");
		// 放入 model 傳送至前端
		model.addAttribute("proOrdStatus", proOrdStatus);
//		========================
		// 設定付款狀態，給前端做下拉式選單
		Map<Integer, String> proPayStatus = new HashMap<>();
		proPayStatus.put(0, "未付款");
		proPayStatus.put(1, "已付款");
		// 放入 model 傳送至前端
		model.addAttribute("proPayStatus", proPayStatus);
	}
}
