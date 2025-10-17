package com.farmtastic.proorder.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.memprocpn.model.MemProCpnRepository;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemId;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;
import com.farmtastic.shoppingcart.model.ShoppingCartService;
import com.farmtastic.validator.RegistrationValidation;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/proorders")
public class ProOrderMemController {

	// Points Earning Rate 單筆訂單點數回饋
	// 計算消費商品的總金額(金額不含運費)
	private final static double POINTS_PER = 0.01;

	// 計算單筆訂單的抽成百分筆
	private static final double ALLOC_PER = 0.1;

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService proOrderItemSvc;
	@Autowired
	MemService memSvc;
	@Autowired
	ShoppingCartService shoppingCartSvc;
	@Autowired
	MemProCpnServiceImp mpcSvc;
	@Autowired
	MemProCpnRepository mpcRepository;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");

		// 錯誤驗證
		try {
			List<ProOrderVO> list = proOrdSvc.getAllByMemId(loggedInMember);

			// 將值回傳至前端thymeleaf
			model.addAttribute("proOrderList", list);
		} catch (Exception e) {
			// TODO: handle exception

		}
		return "/front_end/customer/logined/memProOrders/listAllProOrder";
	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		List<ProOrderItemVO> items = proOrderItemSvc.getProOrderItems(proOrderVO);

		// 將值回傳至前端thymeleaf
		model.addAttribute("proOrderVO", proOrderVO);
		model.addAttribute("proOrderItems", items);

		return "/front_end/customer/logined/memProOrders/listOneProOrder";
	}

	// 進入新增訂單頁面(view)
	@GetMapping("addProOrder")
	public String addProOrder(HttpSession session, ModelMap model) {

		// 取得 session 的會員資訊
		ProOrderVO cartToProOrder = (ProOrderVO) session.getAttribute("cartToProOrder");
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");

		// 如果沒有 cartToProOrder (即非從結帳頁面重定向而來)，直接返回至購物車頁面。
		// 這樣可防止用戶直接訪問此 URL 時發生錯誤
		if (cartToProOrder == null) {
			// 如果沒有暫存訂單，導回購物車頁面
			return "redirect:/cart/view";
		} else {

			// 取得會員的有效折價卷資料
			List<MemProCpnVO> mpcList = mpcSvc.getValidCpnsByMember(loggedInMember.getMemId());

			// 將值回傳至前端thymeleaf
			model.addAttribute("cartToProOrder", cartToProOrder);
			model.addAttribute("mpcList", mpcList);

			return "/front_end/customer/logined/memProOrders/addProOrder";

		}

	}

	// URL: POST /mem/proorders/updatestatus
	/**
	 * 修改訂單的狀態
	 */
	@PostMapping("updatestatus")
	public String proOrderReturn(@RequestParam("proOrdId") Integer proOrdId,
			@RequestParam("proOrdStatus") Integer proOrdStatus, ModelMap model, RedirectAttributes redirectAttributes,
			HttpSession session) {

		// 判斷是否要更新狀態
		boolean updateStatus = false;
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(proOrdId);

		switch (proOrdStatus) {
		// 訂單未出貨，可以直接取消訂單。
		case 0:
		case 1:
			System.out.println("訂單取消！");
			proOrderVO.setProOrdStatus((byte) 1);
			// 取消訂單返回庫存的邏輯
			proOrdSvc.cancelOrderAndBackStock(proOrderVO);

			updateStatus = true;
			redirectAttributes.addFlashAttribute("successMessage", "訂單已經取消！");
			break;
		// 出貨中，通知賣家到貨
		case 2:
			System.out.println("已通知賣家到貨！");
			proOrderVO.setProOrdStatus((byte) 3);
			updateStatus = true;
			redirectAttributes.addFlashAttribute("successMessage", "已通知賣家到貨！");
			break;
		// 已出貨，需要輸入文字，才能申請退貨。
		case 3:
			System.out.println("買家提出退貨申請！");
			proOrderVO.setProOrdStatus((byte) 4);
			updateStatus = true;
			redirectAttributes.addFlashAttribute("successMessage", "已提出退貨申請！");
			break;
		// 訂單已經是退貨流程，直接返回。
		case 4:
			System.out.println("已通知賣家退貨！");
			proOrderVO.setProOrdStatus((byte) 5);
			updateStatus = true;
			redirectAttributes.addFlashAttribute("successMessage", "已通知賣家退貨！");
			break;
		// 已經是退貨狀態，不會更新狀態
		// 已在前端隱藏退貨按鈕，以下判斷為預防用。
		case 5:
		case 6:
			System.out.println("已經是退貨狀態！");
			redirectAttributes.addFlashAttribute("errorMessage", "已經是退貨狀態！");
			break;
		default:
			break;
		}

		if (updateStatus) {
			proOrdSvc.updateProOrder(proOrderVO);
		}

		return "redirect:/mem/proorders/listAllProOrder";
	}

	// URL: POST /mem/proorders/insert
	/**
	 * 處理訂單最終提交的 POST 請求
	 */
	@PostMapping("insert")
	public String insert(
			@Validated(RegistrationValidation.class) @ModelAttribute("cartToProOrder") ProOrderVO proOrderVO,

			BindingResult result, HttpSession session, RedirectAttributes redirectAttributes, ModelMap model) {

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		ProOrderVO sessionOrder = (ProOrderVO) session.getAttribute("cartToProOrder");
		List<ProOrderItemVO> finalItems = sessionOrder.getProOrderItems();

		// 檢查是否有驗證錯誤
		System.out.println("===== 驗證結果 =====");
		System.out.println("是否有錯誤: " + result.hasErrors());
		System.out.println("錯誤數量: " + result.getErrorCount());

		if (result.hasErrors()) {
			System.out.println("===== 錯誤詳情 =====");
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				System.out.println("欄位: " + error.getObjectName());
				System.out.println("錯誤碼: " + error.getCode());
				System.out.println("錯誤訊息: " + error.getDefaultMessage());
				System.out.println("---");
			}

			// 檢查輸入值
			System.out.println("===== 輸入值 =====");
			System.out.println("姓名: [" + proOrderVO.getProOrdName() + "]");
			System.out.println("電話: [" + proOrderVO.getProOrdMobile() + "]");
			System.out.println("Email: [" + proOrderVO.getProOrdEmail() + "]");
			System.out.println("地址: [" + proOrderVO.getProOrdAddr() + "]");

			// 設置必要欄位
			proOrderVO.setProOrderItems(finalItems);
			proOrderVO.setProOrdDate(sessionOrder.getProOrdDate());
			proOrderVO.setProTotal(sessionOrder.getProTotal());
			proOrderVO.setProOrdShipFee(sessionOrder.getProOrdShipFee());
			proOrderVO.setMemVO(loggedInMember);

			// 檢查 Model 屬性
			model.addAttribute("cartToProOrder", proOrderVO);

			// 取得會員的有效折價券資料
			List<MemProCpnVO> mpcList = mpcSvc.getValidCpnsByMember(loggedInMember.getMemId());
			model.addAttribute("mpcList", mpcList);
			model.addAttribute("cartToProOrder", proOrderVO);

			System.out.println("===== Model 屬性 =====");
			System.out.println("cartToProOrder 已添加到 model");
			System.out.println("BindingResult 錯誤數: " + result.getErrorCount());

			return "/front_end/customer/logined/memProOrders/addProOrder";
		}

		// 成功時：使用表單提交的折扣金額（這些是用戶最後確認的值）
		// proOrderVO.getProOrdPointdisc() - 已經包含用戶設定的點數折抵
		// proOrderVO.getProOrdCpndisc() - 已經包含用戶選擇的優惠券折扣
		// proOrderVO.getProOrdGrandTotal() - 已經包含最終計算的實付金額
		// proOrderVO.getProOrdPointGet() - 已經包含回饋點數

		// 從 session 來的必要資料
		proOrderVO.setProOrdDate(sessionOrder.getProOrdDate());
		proOrderVO.setProTotal(sessionOrder.getProTotal());
		proOrderVO.setProOrdShipFee(sessionOrder.getProOrdShipFee());

		if (proOrderVO.getProOrdCpndisc() == null) {
			proOrderVO.setProOrdCpndisc(0);
		}

		if (proOrderVO.getProOrdPointdisc() == null) {
			proOrderVO.setProOrdPointdisc(0);
		}
		if (proOrderVO.getProOrdPointGet() == null) {
			proOrderVO.setProOrdPointGet(0);
		}

		// 檢查是否有使用優惠券
		Integer cpnHolderDetailId = proOrderVO.getMemProCpnVO().getCpnHolderDetailId();
		boolean checkUseMcpn = proOrdSvc.checkUseMcpn(proOrderVO, cpnHolderDetailId);
		if (checkUseMcpn) {
			MemProCpnVO uesedMpc = mpcSvc.getOne(cpnHolderDetailId);
			proOrderVO.setMemProCpnVO(uesedMpc);
		} else {
			proOrderVO.setMemProCpnVO(null);
		}

		// 訂單狀態
		proOrderVO.setProOrdStatus((byte) 0);

		// 訂單付款狀態
		proOrderVO.setProPayStatus((byte) 0);

		// 平台撥款狀態，預設為0(未撥款)
		proOrderVO.setProOrdAllocStatus((byte) 0);

		// 平台抽成金額
		// 依照訂單的商品總金額（不含運不含折扣），計算平台抽成的金額。
		Integer ProOrdAllocTotal = (int) (proOrderVO.getProTotal() * ALLOC_PER);
		proOrderVO.setProOrdAllocTotal(ProOrdAllocTotal);

		// 平台撥款給小農的金額
		Integer proOrdAllocSendFmem = proOrderVO.getProTotal() - proOrderVO.getProOrdAllocTotal();
		proOrderVO.setProOrdAllocSendFmem(proOrdAllocSendFmem);

		// 設定關聯和明細
		proOrderVO.setMemVO(loggedInMember);
		proOrderVO.setProOrderItems(finalItems);

		// 建立雙向關聯
		for (ProOrderItemVO item : finalItems) {

			item.setProOrderVO(proOrderVO);

			ProOrderItemId id = item.getId();
			if (id == null) {
				id = new ProOrderItemId();
			}
			id.setProId(item.getProductVO().getProId());
			item.setId(id);

		}

		// ================== (先預扣)扣商品庫存的邏輯 ======================
		Pro errorProStock = proOrdSvc.discProductStock(proOrderVO);
		if (errorProStock != null) {
			// 返回購物車，修正數量。
			redirectAttributes.addFlashAttribute("errorMessage", "商品[ " + errorProStock.getProName() + " ]數量不足，無法購買！");
			return "redirect:/mem/cart/view/";
		}
		// ================== 新增訂單 =====================
		try {
			proOrdSvc.addProOrder(proOrderVO);

		} catch (RuntimeException e) {
			// 捕捉 Service 拋出的商品 ID 缺失或其他錯誤
			model.addAttribute("errorMessage", "新增訂單失敗：" + e.getMessage());
			model.addAttribute("memVO", loggedInMember);
			proOrderVO.setProOrderItems(finalItems);
			model.addAttribute("cartToProOrder", proOrderVO);
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}

		// ================= 根據付款不同導向不同頁面 ==================
		// 取得新增訂單後的 proOrdId
		Integer newProOrdId = proOrderVO.getProOrdId();

		switch (proOrderVO.getProOrdPayment()) {
		case 0: // 信用卡
			// 先暫時導向首頁
			session.setAttribute("proOrdIdByPay", newProOrdId);
			return "redirect:/mem/proorders/dopay";
		case 1: // LinePay
			return "redirect:/mem/proorders/linepayview?proOrdId=" + newProOrdId;
		default: // 未新增訂單
			//有其他不明錯誤，直接刪除訂單，重新下單。
			proOrdSvc.deleteProOrder(newProOrdId);
			return "redirect:/cart/view";
		}
	}

	// 確定訂單付款後，才開始做修改訂單的邏輯
	@GetMapping("dopay")
	public String doPay(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		Integer proOrdIdByPay = (Integer) session.getAttribute("proOrdIdByPay");
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(proOrdIdByPay);

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		ProOrderVO sessionOrder = (ProOrderVO) session.getAttribute("cartToProOrder");

		// ================== 付款狀態修改狀態 ======================
		if (proOrderVO.getProPayStatus() == 0) {
			// 修改已付款(1)
			proOrderVO.setProPayStatus((byte) 1);
			proOrdSvc.updateProOrder(proOrderVO);
		}

		// ================== 會員點數新增修改的邏輯 ======================
		// 從proOrderVO取得此訂單的回饋點數，儲存至mem物件的會員點數欄位
		Integer memPoint = proOrderVO.getMemVO().getMemPoint();
		Integer memPointDisc = proOrderVO.getProOrdPointdisc();
		Integer memPointGet = proOrderVO.getProOrdPointGet();
		Integer finalMemPoint = memPoint - memPointDisc + memPointGet;
		loggedInMember.setMemPoint(finalMemPoint);

		// 將最終點數結果，存回DB
		memSvc.updateMem(loggedInMember);

		// ================== 折價卷修改狀態 ======================
		if (proOrderVO.getMemProCpnVO() != null) {
			try {
				MemProCpnVO updateMpc = mpcSvc.getOne(proOrderVO.getMemProCpnVO().getCpnHolderDetailId());
				// 設定已經使用該折價券
				updateMpc.setCpnUseStatus(CpnUseStatus.USED);
				// 將最終點數結果，存回DB
				mpcSvc.updateMemProCpn(updateMpc);
			} catch (Exception e) {
				// 記錄錯誤但不影響訂單流程
				System.err.println("更新優惠券狀態失敗: " + e.getMessage());
			}
		}

		// 更新網頁會員的session的資料
		session.setAttribute("loggedInMember", loggedInMember);

		// 清除 Session 相關屬性
		session.removeAttribute("cartToProOrder");
		session.removeAttribute("proOrdIdByPay");

		// 清除 該訂單的購物車內容
		// 因為確定這份訂單內的產品，都是來自同一個小農fmemId
		// 所以直接找集合內的第一個物件，取出fmemId
		Integer fmemId = proOrderVO.getProOrderItems().get(0).getProductVO().getFmemId().getFmemId();
		shoppingCartSvc.clearCartByFmemId(fmemId);

		redirectAttributes.addFlashAttribute("successMessage", "新的訂單已成功建立！");
		return "redirect:/mem/proorders/listAllProOrder";

	}

	// 修改訂單 (處理點數折抵及折價卷折抵)
	@PostMapping("OrdDiscUpdate")
	public String update(@RequestParam(name = "proOrdPointdisc", required = false) String proOrdPointdiscStr,
			@RequestParam(name = "memProCpnVO.cpnHolderDetailId", required = false) Integer cpnHolderDetailId,
			HttpSession session, ModelMap model, RedirectAttributes redirectAttributes) {

		// 取得 session 中的必要資訊
		ProOrderVO finalProOrderVO = (ProOrderVO) session.getAttribute("cartToProOrder");
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");

		// 設定錯誤狀態，有錯誤返回
		boolean hasError = false;

		if (finalProOrderVO == null) {
			// 如果沒有暫存訂單，直接導回購物車
			return "redirect:/cart/view";
		}
		// ==============會員點數錯誤驗證==============
		// 將 finalProOrderVO 重新放回 Model，以便在驗證失敗時，其他訂單資訊能被保留
		// 雖然這個請求最終是 redirect，但為了在發生錯誤時能直接 return 頁面，我們先放
		model.addAttribute("cartToProOrder", finalProOrderVO);

		Integer tempPointdisc = 0; // 用於儲存有效的點數折抵值

		// 1. 驗證空值/無效輸入
		if (proOrdPointdiscStr == null || proOrdPointdiscStr.trim().isEmpty()) {
			// 如果為空，我們將其視為 0 折抵，並繼續執行，或您可以選擇添加錯誤
			tempPointdisc = 0;
		} else {
			try {
				tempPointdisc = Integer.valueOf(proOrdPointdiscStr);
			} catch (NumberFormatException e) {
				// 處理非數字輸入
				model.addAttribute("pointDiscError", "請輸入有效的數字作為折抵點數。");
				model.addAttribute("inputPointDisc", proOrdPointdiscStr); // 保留錯誤輸入值
				hasError = true;
			}
		}

		// 如果有格式錯誤，直接返回
		if (hasError) {
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}

		// 2. 驗證點數的邏輯
		Integer memPoint = loggedInMember.getMemPoint();
		Integer proTotal = finalProOrderVO.getProTotal();
		Integer pointCpndisc = finalProOrderVO.getProOrdCpndisc() != null ? finalProOrderVO.getProOrdCpndisc() : 0;

		// ==============會員折價卷數錯誤驗證==============
		// 取會員所選取的折價卷物件
		if (cpnHolderDetailId == 0 && tempPointdisc == 0) {
			// 由於我們是手動返回頁面，必須確保將用戶輸入的值也放回 Model
			model.addAttribute("inputPointDisc", tempPointdisc);
			// 取得會員的有效折價卷資料
			List<MemProCpnVO> mpcList = mpcSvc.getValidCpnsByMember(loggedInMember.getMemId());
			model.addAttribute("mpcList", mpcList);
			redirectAttributes.addFlashAttribute("successMessage", "未使用折扣（點數or折價劵）");
			return "redirect:addProOrder";
		}
		Optional<MemProCpnVO> tempMpcVO = mpcRepository.findById(cpnHolderDetailId);
		MemProCpnVO mpcVO = tempMpcVO.orElse(new MemProCpnVO());
		// 設定折價卷折抵變數
		BigDecimal mpcDisc = null;
		Double tempMpcDisc = 0.0;
		Integer finalMpcDisc = 0;

		if (cpnHolderDetailId != 0) {
			// 0 為未選取折假劵，所以不更新。

			mpcDisc = mpcVO.getProCpnVO().getDiscValue();

			// 取該折價卷的所屬種類（扣金額or百分比）
			switch ((mpcVO.getProCpnVO().getDiscType())) {
			// 金額折抵
			case FULL_REDUCTION:
				// 折價卷折抵後的金額
				tempMpcDisc = mpcDisc.doubleValue();
				finalMpcDisc = tempMpcDisc.intValue();
				break;
			// 百分比折抵
			case PERCENTAGE:
				// 折價卷折抵後的金額
				tempMpcDisc = proTotal.doubleValue() * mpcDisc.doubleValue();
				finalMpcDisc = proTotal - tempMpcDisc.intValue();
				break;
			default:
				break;
			}
		}

		// 3. 處理負數、超過持有/超過最高可折抵金額
		// 可用來折抵的最高金額 (商品總金額 - 折價券折抵)
		Integer maxDiscAmount = proTotal - pointCpndisc - finalMpcDisc;

		if (tempPointdisc < 0) {
			model.addAttribute("pointDiscError", "折抵點數不能是負數。");
			hasError = true;
		} else if (tempPointdisc > memPoint) {
			model.addAttribute("pointDiscError", "您的折抵點數 (" + tempPointdisc + ") 超過您持有的總點數 (" + memPoint + ")。");
			hasError = true;
		} else if (tempPointdisc > maxDiscAmount && tempPointdisc != 0) {
			model.addAttribute("pointDiscError", "折抵點數不能超過\"商品\"實付金額 ($" + maxDiscAmount + ")。");
			hasError = true;
		}

		if (tempMpcDisc < 0) {
			model.addAttribute("cpnError", "折價劵折抵金額不能是負數。");
			hasError = true;
		} else if (tempMpcDisc > maxDiscAmount && tempPointdisc != 0) {
			model.addAttribute("cpnError", "折價劵折抵金額不能超過\"商品\"實付金額 ($" + maxDiscAmount + ")。");
			hasError = true;
		}

		// 如果有邏輯錯誤，返回原頁面
		if (hasError) {
			// 儲存新的欄位狀態(保存已選取的欄位狀態)
			mpcVO.setCpnHolderDetailId(cpnHolderDetailId);
			finalProOrderVO.setMemProCpnVO(mpcVO);
			model.addAttribute("cartToProOrder", finalProOrderVO);
			// 由於我們是手動返回頁面，必須確保將用戶輸入的值也放回 Model
			model.addAttribute("inputPointDisc", tempPointdisc);
			// 取得會員的有效折價卷資料
			List<MemProCpnVO> mpcList = mpcSvc.getValidCpnsByMember(loggedInMember.getMemId());
			model.addAttribute("mpcList", mpcList);

			return "/front_end/customer/logined/memProOrders/addProOrder";
		}

		// 4. 成功執行 (原有的邏輯)

		// 實際折抵金額 (假設 1 點 = 1 元)
		Integer pointDiscountAmount = tempPointdisc;

		// (1) 更新折抵點數
		finalProOrderVO.setProOrdPointdisc(pointDiscountAmount);

		// (2) 計算新的實付金額 (Grand Total)
		Integer proOrdShipFee = finalProOrderVO.getProOrdShipFee() != null ? finalProOrderVO.getProOrdShipFee() : 0;
		Integer proOrdGrandTotal = proTotal + proOrdShipFee - pointCpndisc - pointDiscountAmount - finalMpcDisc;
		finalProOrderVO.setProOrdGrandTotal(proOrdGrandTotal);

		// (3) 計算新的回饋點數
		Integer proOrdPointGet = (int) (proOrdGrandTotal * POINTS_PER);
		finalProOrderVO.setProOrdPointGet(proOrdPointGet);

		// (4) 儲存新的欄位狀態(保存已選取的欄位狀態)
		MemProCpnVO finalMpcVO = mpcVO;
		finalMpcVO.setCpnHolderDetailId(cpnHolderDetailId);
		finalProOrderVO.setMemProCpnVO(finalMpcVO);
		finalProOrderVO.setProOrdCpndisc(finalMpcDisc);

		// 5. 將更新後的訂單物件存回 model
		model.addAttribute("cartToProOrder", finalProOrderVO);
		model.addAttribute("proOrderItems", finalProOrderVO.getProOrderItems());

		session.setAttribute("cartToProOrder", finalProOrderVO);

		// 取得會員的有效折價卷資料
		List<MemProCpnVO> mpcList = mpcSvc.getValidCpnsByMember(loggedInMember.getMemId());
		model.addAttribute("mpcList", mpcList);

		// 6. 成功重定向
		model.addAttribute("successMessage", "折抵" + cpnHolderDetailId + "已更新！" + finalMpcDisc);
		// redirectAttributes.addFlashAttribute("successMessage", "未選擇折價卷！");
		return "/front_end/customer/logined/memProOrders/addProOrder";
	}
}

	// 功能展示用，重新付款流程
	// 本專案的業務邏輯，下訂單後一定要先付款，才會有產生訂單資料存回DB
