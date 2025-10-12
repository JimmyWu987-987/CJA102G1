package com.farmtastic.proimage.controller;

import org.springframework.stereotype.Controller;
	import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;

import java.io.IOException;

@Controller
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ProImageService proImageService;

    /**
     * 根據產品圖片的 ID (pro_img_id) 提供圖片。
     * @param proImgId 產品圖片的 ID。
     * @return 圖片的原始 byte[] 資料。
     */
    @GetMapping(value = "/product-image/{proImgId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> getImageByImageId(@PathVariable("proImgId") Long proImgId) {
        ProImage productImage = proImageService.getProImageById(proImgId);
        
        if (productImage != null && productImage.getProImg() != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productImage.getProImg());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

