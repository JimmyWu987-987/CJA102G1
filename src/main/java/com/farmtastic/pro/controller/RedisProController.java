package com.farmtastic.pro.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proad.model.ProAdService;
import com.farmtastic.procate.model.Procate;
import com.farmtastic.procate.model.ProcateService;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.procom.model.ProComVO;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mall")
public class RedisProController {

    @Autowired
    ProService proSvc;

    @Autowired
    ProcateService procateSvc;

    @Autowired
    ProImageService proImageSvc;
    
    @Autowired
    private ProAdService proAdService;
    
    @Autowired
    private ProComService proComSvc;

    @GetMapping("/products")
    public String listProducts(HttpServletRequest req, Model model, @RequestParam(defaultValue = "0") int page) {
        // 每頁顯示的項目數量
        int pageSize = 4;
        
        // 獲取查詢參數
        Map<String, String[]> map = req.getParameterMap();
        List<Pro> allProducts = proSvc.getAll(map);

        // 手動進行分頁
        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalProducts);
        
        List<Pro> paginatedProducts = allProducts.subList(start, end);

        // 為分頁後的商品找到它的第一張圖片
        paginatedProducts.forEach(pro -> {
            proImageSvc.findFirstImageByProId(pro.getProId().longValue())
                       .ifPresent(pro::setProImage);
        });
        
        // 將查詢參數轉換為 URL 字串，用於分頁連結
        String queryParams = map.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue()[0])
            .collect(Collectors.joining("&"));

        model.addAttribute("proListData", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("queryParams", queryParams != null ? queryParams : "");
        
      //商城廣告圖片
        model.addAttribute("adIds", proAdService.getPassProAds());
        
        return "front_end/customer/unlogined/shopmall/mall";
    }
    
    /**
     * 處理顯示單一商品詳細資料的請求。
     * @param proId 商品ID
     * @param model Model
     * @return 返回商品詳細頁面
     */
    @GetMapping("/product/{proId}")
    public String showProductDetail(@PathVariable("proId") Integer proId, Model model, HttpSession session) {
    	
        Map<String, String[]> map = Map.of("proId", new String[]{String.valueOf(proId)});
        List<Pro> result = proSvc.getAll(map);
        
        Pro pro = null;
        if (result != null && !result.isEmpty()) {
            pro = result.get(0);
            
            List<ProImage> allImages = proImageSvc.findAllImagesByProId(pro.getProId().longValue());
            model.addAttribute("productImages", allImages);
            
            // 獲取商品的第一張圖片
            proImageSvc.findFirstImageByProId(pro.getProId().longValue())
                       .ifPresent(pro::setProImage);
        
        
        // 獲取該商品的所有評論
            try {
                // 獲取該商品的所有評論
                
                List<ProComVO> comments = proComSvc.getProComByProVO(pro);
                
                // === Debug: 印出評論資料 ===
                System.out.println("=== 評論查詢結果 ===");
                System.out.println("商品ID: " + pro.getProId());
                System.out.println("評論數量: " + (comments != null ? comments.size() : 0));
                if (comments != null && !comments.isEmpty()) {
                    for (int i = 0; i < comments.size(); i++) {
                        ProComVO c = comments.get(i);
                        System.out.println("評論 " + (i+1) + ":");
                        System.out.println("  - 評論ID: " + c.getProComId());
                        System.out.println("  - 會員: " + (c.getMemVO() != null ? c.getMemVO().getMemName() : "null"));
                        System.out.println("  - 內容: " + c.getProComContent());
                        System.out.println("  - 評分: " + c.getProComRate());
                        System.out.println("  - 時間: " + c.getProComTime());
                    }
                }
                System.out.println("===================");
                
                
                // 手動排序：最新的評論在前面
                if (comments != null && !comments.isEmpty()) {
                    comments.sort((c1, c2) -> c2.getProComTime().compareTo(c1.getProComTime()));
                }
                
                model.addAttribute("comments", comments);
            } catch (Exception e) {
                // 如果評論查詢失敗，記錄錯誤但不影響頁面顯示
                System.err.println("查詢評論時發生錯誤: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("comments", List.of());
            }
            
            // 檢查使用者是否已登入
            Mem loginMem = (Mem) session.getAttribute("loginMem");
            boolean isUserLoggedIn = (loginMem != null);
            model.addAttribute("isUserLoggedIn", isUserLoggedIn);
            // ===== 評論查詢功能結束 =====
        }
        
        model.addAttribute("pro", pro);
        return "front_end/customer/unlogined/shopmall/prodetail"; // 指向新的商品詳細頁面
    }


    /**
     * 提供商品分類列表給前端的查詢表單使用。
     * @return 商品分類列表。
     */
    @ModelAttribute("procateListData")
    public List<Procate> populateProcateList() {
        return procateSvc.getAll();
    }
    
//    @PostMapping("/cart/add")
//    public ResponseEntity<String> addToCart(@RequestParam("proId") Integer proId) {
//        // 在這裡，您應該加入實際的購物車邏輯，例如：
//        // 1. 檢查使用者是否登入
//        // 2. 獲取購物車物件 (可能來自 Session 或資料庫)
//        // 3. 將 proId 和數量加入購物車
//        // 4. 更新購物車狀態
//        System.out.println("成功將商品 #" + proId + " 加入購物車！");
//        return ResponseEntity.ok("加入成功");
//    }
}
