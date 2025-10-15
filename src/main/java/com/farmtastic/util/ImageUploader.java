
package com.farmtastic.util;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ImageUploader {
    
    // 修改成你的資料庫密碼
    private static final String PASSWORD = "123456";
    
    public static void main(String[] args) {
        System.out.println("開始上傳圖片...\n");
        
        // 方式1: 每個 ID 對應不同圖片 (檔名要有數字)
        // 上傳小農圖片
        uploadImages("fmem", "UPDATE fmem SET fmem_pic = ? WHERE fmem_id = ?");
        
        // 上傳商店圖片
        uploadImages("store", "UPDATE fmem SET store_pic = ? WHERE fmem_id = ?");
        
        // 方式2: 用同一張圖上傳到所有 ID
        // 上傳土地證明圖片
        uploadSameImage("land/template.png", "UPDATE fmem SET land_pic = ? WHERE fmem_id BETWEEN 1 AND 20");
        
        // 上傳上傳農保圖片
        uploadSameImage("insur/template.png", "UPDATE fmem SET insur_pic = ? WHERE fmem_id BETWEEN 1 AND 20");
        
        System.out.println("\n上傳完成！");
    }
    
    private static void uploadImages(String folder, String sql) {
        try {
            // 取得專案路徑下的圖片資料夾
            File dir = new File("src/main/resources/static/images/sql/" + folder);
            
            if (!dir.exists()) {
                System.out.println("找不到資料夾: " + folder);
                return;
            }
            
            File[] files = dir.listFiles((d, name) -> 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".png") ||
                name.toLowerCase().endsWith(".jpeg"));
            
            if (files == null || files.length == 0) {
                System.out.println(folder + ": 沒有圖片");
                return;
            }
            
            // 連接資料庫
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/farmtastic?useSSL=false&serverTimezone=Asia/Taipei",
                "root", 
                PASSWORD
            );
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            int count = 0;
            for (File file : files) {
                try {
                    // 從檔名取得 ID (例如: fmem_1.jpg -> 1)
                    String name = file.getName();
                    String num = name.replaceAll("[^0-9]", "");
                    int id = Integer.parseInt(num);
                    
                    // 讀取圖片
                    byte[] image = Files.readAllBytes(file.toPath());
                    
                    // 設定參數並執行
                    pstmt.setBytes(1, image);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    
                    count++;
                    System.out.println("✓ " + file.getName());
                    
                } catch (Exception e) {
                	e.printStackTrace();
                    System.out.println("✗ " + file.getName() + " (錯誤)");
                }
            }
            
            pstmt.close();
            conn.close();
            
            System.out.println(folder + ": 上傳 " + count + " 張\n");
            
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("錯誤: " + e.getMessage());
        }
    }
    
    
    
 // 用同一張圖上傳到所有符合條件的資料
    private static void uploadSameImage(String imagePath, String sql) {
        try {
            File file = new File("src/main/resources/static/images/sql/" + imagePath);
            
            if (!file.exists()) {
                System.out.println("找不到圖片: " + imagePath);
                return;
            }
            
            byte[] image = Files.readAllBytes(file.toPath());
            
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/farmtastic?useSSL=false&serverTimezone=Asia/Taipei",
                "root", 
                PASSWORD
            );
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBytes(1, image);
            int count = pstmt.executeUpdate();
            
            pstmt.close();
            conn.close();
            
            System.out.println("✓ " + file.getName() + " 上傳到 " + count + " 筆資料\n");
            
        } catch (Exception e) {
            System.out.println("錯誤: " + e.getMessage());
        }
    }
    
    
}