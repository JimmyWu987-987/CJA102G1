package com.farmtastic.proorder.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.member.model.Mem;
import com.farmtastic.product.model.Product;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem/proorders")
public class ProOrderMemController {

	// Points Earning Rate
	// 計算消費商品的總金額(金額不含運費)
	private final static double PER = 0.01;

	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService proOrderItemSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");

		// 錯誤驗證
		if (loggedInMember == null || memId == null || memName == null || memName.trim().isEmpty()) {
			// 沒有值則會重導至登入頁面
			return "redirect:/mem/showMemRegLoginForm";
		} else {
			try {
				Mem MemVO = new Mem();
				MemVO.setMemId(memId);
				List<ProOrderVO> list = proOrdSvc.getAllByMemId(MemVO);

				// 將值回傳至前端thymeleaf
				model.addAttribute("proOrderList", list);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return "/front_end/customer/logined/memProOrders/listAllProOrder";
		}
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

	// 進入新增訂單頁面
	@GetMapping("addProOrder")
	public String addProOrder(HttpSession session, ModelMap model) {

		// 取得 session 的會員資訊
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = (Integer) session.getAttribute("memId");
		String memName = (String) session.getAttribute("memName");

		// 錯誤驗證
		if (loggedInMember == null || memId == null || memName == null || memName.trim().isEmpty()) {
			// 沒有值則會重導至登入頁面
			return "redirect:/mem/showMemRegLoginForm";
		} else {
			// 建立商品訂單
			ProOrderVO proOrderVO = new ProOrderVO();

			// 將session的值儲存至 proOrderVO.memVO.memId
			Mem memVO = new Mem();
			// 設定 memVO 的 memId
			// 將包含 memId 的 memVO 設定給 proOrderVO
			memVO.setMemId(memId);
			proOrderVO.setMemVO(memVO);

			// 查詢該會員"未使用"的"全部"商品折價卷明細
			// 儲存 商品折價卷明細 的 商品折價卷編號

			// 新增訂單日期為當下系統時間
			// 讀取毫秒
			// 將日期格式轉成 yyyy-MM-dd HH:mm:ss，由JPA處理日期格式(ProOrderVO第47行)
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
			// 存入proOrderVO物件
			proOrderVO.setProOrdDate(currentTimestamp);

			// 訂單狀態預設為(0:成立訂單)
			proOrderVO.setProOrdStatus((byte) 0);
			// 付款狀態預設為(0:未付款)
			proOrderVO.setProPayStatus((byte) 0);

			// 顯示從購物車傳入的商品訂單明細
			// 將購物車的session 存入商品訂單明細
			// 這邊我先手動輸入，等購物車做好再改成session取值
			Product p1 = new Product("新鮮杏鮑菇", 75, 85, 1, 4, 190, "苗栗");
			Product p2 = new Product("在地小番茄", 140, 70, 1, 5, 260, "桃園");
			// 未完成
			List<ProOrderItemVO> items = new LinkedList<ProOrderItemVO>();
			
			

			// 計算商品總金額
			Integer proTotal = null;
			if (proTotal == null) {
				proTotal = 9487;
			}
			// 存入proOrderVO物件
			proOrderVO.setProTotal(proTotal);

			// 計算運費金額
			Fmem fmem = new Fmem();
			// 查詢小農的運費
			// 這邊要寫一個fmem的service的方法
			// 1.等同學寫好小農的單一查詢。
			// 2.再從小農編號查詢該運費
			
			
			Integer prodFee = fmem.getProdFee();
			// 判斷運費欄位是否為null
			if (prodFee == null) {
				prodFee = 0; // 如果小農沒設定運費，則預設為0
			}
			fmem.setProdFee(prodFee);

			// 折價券折抵金額
			// 用memId查詢 同學寫好持有者明細
			// 等同學寫好持有者明細
			proOrderVO.setProOrdCpndisc(null);
			Integer proOrdCpndisc = proOrderVO.getProOrdCpndisc(); // 先手動輸入
			if(proOrdCpndisc == null) {
				proOrdCpndisc = 0;
			}
			

			// 會員持有點數
			Integer memPoint = loggedInMember.getMemPoint();

			// 商品訂單折抵會員點數
			Integer proOrdPointdisc = proOrderVO.getProOrdPointdisc();
			if(proOrdPointdisc == null) {
				proOrdPointdisc = 0;
			}
			proOrderVO.setProOrdPointdisc(proOrdPointdisc);
			// 修改該會員點數
			// 這邊要寫一個修改mem的service
			memPoint = memPoint - proOrdPointdisc;
			memVO.setMemPoint(memPoint);
			
			// 實付金額
			// 實付金額 = 商品總金額 + 運費 - 折價券折抵金額 - 訂單折抵會員點數
			Integer proOrdGrandTotal = proTotal + prodFee - proOrdCpndisc - proOrdPointdisc;
			proOrderVO.setProOrdGrandTotal(proOrdGrandTotal);
			
			// 訂單回饋會員點數
			// 回饋 1%
			// 無條件捨去小數點
			Integer proOrdPointGet = (int) (Math.floor(proTotal * PER));
			proOrderVO.setProOrdPointGet(proOrdPointGet);
			
			// 物流追蹤碼
			// 小農前台做修改
			// 預設可以null
			
			// 出貨日期
			// 小農前台做修改
			// 預設可以null

			// 收件人姓名
			proOrderVO.setProOrdName(loggedInMember.getMemName());
			
			// 收件人電話
			proOrderVO.setProOrdMobile(loggedInMember.getMemMobile());
			
			// 收件人電子郵件
			proOrderVO.setProOrdEmail(loggedInMember.getMemEmail());
			
			// 收件人地址
			String proOrdAddr = loggedInMember.getMemZipcode();
			proOrdAddr += loggedInMember.getMemCity();
			proOrdAddr += loggedInMember.getMemDist();
			proOrdAddr += loggedInMember.getMemAddr();
			proOrderVO.setProOrdAddr(proOrdAddr);
			
			
			// 將值回傳至前端thymeleaf
			model.addAttribute("memVO", memVO);
			model.addAttribute("fmemVO", fmem);
			model.addAttribute("proOrderVO", proOrderVO);
			model.addAttribute("proOrderItemVO", items);

			return "/front_end/customer/logined/memProOrders/addProOrder";
		}
	}

	// 新增訂單
	@PostMapping("insert")
	public String insert(@Valid ProOrderVO proOrderVO, @Valid ProOrderItemVO proOrderItemVO, BindingResult result,
			HttpSession session, ModelMap model) {

		proOrderVO.setMemVO(null);

		// 輸入資料的錯誤驗證
		if (result.hasErrors()) {
			// 數入資料錯誤，重新返回訂單頁面
			return "/front_end/customer/logined/memProOrders/addProOrder";
		}
		// 驗證成功後，新增資料
		proOrdSvc.addProOrder(proOrderVO);
		proOrderItemSvc.addProOrderItem(proOrderItemVO);
		// 因為要計算會員持有點數，還要寫一個修改Mem的service方法
		
		// 將資料交給資料庫
		

		return "/front_end/customer/logined/memProOrders/addProOrder";
	}
}
