package com.farmtastic.proimage.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;



@Controller
@RequestMapping("/pro-images")
public class ProImageController {

    @Autowired
    private ProImageService proImageService;

    @Autowired
    private ProService proService;

    /**
     * 顯示指定產品的圖片管理頁面
     * @param proId 產品ID
     */
    @GetMapping("/manage/{proId}")
    public String showImageManagementPage(@PathVariable("proId") Integer proId, Model model) {
        // 查詢產品基本資料，用於在頁面上顯示產品名稱
        Pro pro = proService.getOnePro(proId);
        // 查詢該產品的所有圖片
        List<ProImage> images = proImageService.findAllImagesByProId(proId.longValue());

        model.addAttribute("pro", pro);
        model.addAttribute("imageList", images);
        return "back_end/pro/ManageProImage"; // 返回新的管理頁面
    }

    /**
     * 為指定產品上傳新圖片
     * @param proId 產品ID
     * @param file 上傳的圖片檔案
     */
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("proId") Integer proId,
                              @RequestParam("imageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "上傳失敗，請選擇一個檔案。");
            return "redirect:/pro-images/manage/" + proId;
        }

        try {
            ProImage newImage = new ProImage();
            newImage.setProId(proId.longValue());
            newImage.setProImg(file.getBytes());
            proImageService.createProductImage(newImage);
            redirectAttributes.addFlashAttribute("successMessage", "圖片上傳成功！");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "檔案讀取失敗，請稍後再試。");
        }

        return "redirect:/pro-images/manage/" + proId;
    }

    /**
     * 刪除指定的圖片
     * @param proImgId 要刪除的圖片ID
     * @param proId 該圖片所屬的產品ID，用於刪除後跳轉回原頁面
     */
    @PostMapping("/delete")
    public String deleteImage(@RequestParam("proImgId") Long proImgId,
                              @RequestParam("proId") Integer proId,
                              RedirectAttributes redirectAttributes) {

        proImageService.deleteProImage(proImgId);
        redirectAttributes.addFlashAttribute("successMessage", "圖片刪除成功！");

        return "redirect:/pro-images/manage/" + proId;
    }
}