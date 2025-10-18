package com.farmtastic.proorder.controller;

import java.util.Map;
import java.util.TreeMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

// 假設的綠界工具類別，用於處理 CheckMacValue 計算
// *** 實際開發中，您需要確保這個類別完整實作了綠界指定的加密和編碼規則 ***
class ECPayUtils {
    // 測試環境參數
    private static final String HASH_KEY = "pwFHCqoQZGmho4w6";
    private static final String HASH_IV = "EkRm7iFT261dpevs";

    /**
     * 計算綠界所需的 CheckMacValue
     * (這裡提供一個精簡的示意，實際專案中建議使用官方SDK或嚴謹封裝)
     * 步驟：1. 參數排序 (A-Z) -> 
     * 2. 串接 HashKey & HashIV -> 
     * 3. URL Encode -> 
     * 4. SHA256 加密 -> 
     * 5. 轉大寫
     */
    public static String generateCheckMacValue(Map<String, String> params) throws Exception {
        // 1. 參數排序 (TreeMap 自動按 Key 字母順序排序)
        TreeMap<String, String> sortedParams = new TreeMap<>(params);

        // 2. 串接 HashKey 和 HashIV
        StringBuilder sb = new StringBuilder();
        sb.append("HashKey=").append(HASH_KEY).append("&");
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.append("HashIV=").append(HASH_IV);

        // 3. URL Encode (綠界使用特定的編碼規則，與標準 Java URLEncoder 可能有差異)
        // 這裡我們用標準 URLEncoder 作為示意，實際可能需要客製化
        String preHash = urlEncode(sb.toString().toLowerCase()); 

        // 4. SHA256 加密
        String checkMacValue = sha256(preHash).toUpperCase();
        
        return checkMacValue;
    }
    
    // 簡化的 URL 編碼 (注意：綠界的 URL 編碼規則較為特殊)
    private static String urlEncode(String str) throws UnsupportedEncodingException {
        // 綠界特有的編碼替換 (請依官方文件實作)
        // 例如：Replace(str, "%2d", "-"); Replace(str, "%5f", "_"); ...
        return URLEncoder.encode(str, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\*", "%2a")
                .replaceAll("~", "%7e")
                .replaceAll("\\(", "%28")
                .replaceAll("\\)", "%29");
    }

    // SHA256 加密
    private static String sha256(String base) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

public class TestEcPay {

    // 介接網址
    private static final String API_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";
    // 測試特店編號
    private static final String MERCHANT_ID = "3002607"; 

    /**
     * 產生導向綠界所需的 HTML 表單
     * @param orderId 訂單編號
     * @param amount 交易金額
     * @param itemName 商品名稱
     * @param returnUrl 綠界付款成功後，Server 端回傳通知的網址
     * @return 包含自動提交腳本的 HTML 完整字串
     */
    public String generateECPayForm(String orderId, int amount, String itemName, String returnUrl) throws Exception {
        // 1. 建立所有傳輸參數
        Map<String, String> params = new TreeMap<>();
        params.put("MerchantID", MERCHANT_ID);
        params.put("MerchantTradeNo", orderId); // 訂單編號 (唯一值)
        params.put("MerchantTradeDate", java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(java.time.LocalDateTime.now()));
        params.put("PaymentType", "aio"); // 交易類型，固定 AIO
        params.put("TotalAmount", String.valueOf(amount)); // 交易金額
        params.put("TradeDesc", "測試商品交易"); // 交易描述
        params.put("ItemName", itemName); // 商品名稱
        params.put("ReturnURL", returnUrl); // 付款完成通知回傳網址 (Server端)
        params.put("ChoosePayment", "ALL"); // 選擇付款方式 (ALL: 全部)
        params.put("EncryptType", "1"); // 加密類型，固定 1 (SHA256)

        // 2. 計算 CheckMacValue
        String checkMacValue = ECPayUtils.generateCheckMacValue(params);
        params.put("CheckMacValue", checkMacValue);

        // 3. 建立 HTML 表單
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='utf-8'></head><body>");
        html.append("<form id='ecpayform' method='post' action='").append(API_URL).append("'>");
        
        // 將所有參數放入隱藏欄位
        for (Map.Entry<String, String> entry : params.entrySet()) {
            html.append("<input type='hidden' name='").append(entry.getKey()).append("' value='").append(entry.getValue()).append("'>");
        }
        
        // 自動提交腳本
        html.append("</form>");
        html.append("<script type='text/javascript'>document.getElementById('ecpayform').submit();</script>");
        html.append("</body></html>");

        return html.toString();
    }
    
    // 範例主程式 (在您的 Web 專案中，這個方法會被 Controller 呼叫)
    public static void main(String[] args) {
    	TestEcPay service = new TestEcPay();
        try {
            // 假設的訂單資訊
            String orderId = "TEST" + System.currentTimeMillis();
            int amount = 100;
            String itemName = "Java課程費用";
            String returnUrl = "http://localhost:8080/mem/proorders/listAllProOrder"; // 替換成您自己的後端接收網址

            String formHtml = service.generateECPayForm(orderId, amount, itemName, returnUrl);
            
            // 在 Web 應用程式中，您會將 formHtml 作為 HTTP Response 傳回給瀏覽器
            System.out.println("--- 產生的自動提交 HTML 表單 ---");
            System.out.println(formHtml);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
