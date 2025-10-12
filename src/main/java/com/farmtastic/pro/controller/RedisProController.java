package com.farmtastic.pro.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procate.model.Procate;
import com.farmtastic.procate.model.ProcateService;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mall")
public class RedisProController {

    @Autowired
    ProService proSvc;

    @Autowired
    ProcateService procateSvc;

    @Autowired
    ProImageService proImageSvc;

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
        
        return "front_end/customer/unlogined/shopmall/mall";
    }
    
    /**
     * 處理顯示單一商品詳細資料的請求。
     * @param proId 商品ID
     * @param model Model
     * @return 返回商品詳細頁面
     */
    @GetMapping("/product/{proId}")
    public String showProductDetail(@PathVariable("proId") Integer proId, Model model) {
    	
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
    
    @PostMapping("/cart/add")
    public ResponseEntity<String> addToCart(@RequestParam("proId") Integer proId) {
        // 在這裡，您應該加入實際的購物車邏輯，例如：
        // 1. 檢查使用者是否登入
        // 2. 獲取購物車物件 (可能來自 Session 或資料庫)
        // 3. 將 proId 和數量加入購物車
        // 4. 更新購物車狀態
        System.out.println("成功將商品 #" + proId + " 加入購物車！");
        return ResponseEntity.ok("加入成功");
    }
}
