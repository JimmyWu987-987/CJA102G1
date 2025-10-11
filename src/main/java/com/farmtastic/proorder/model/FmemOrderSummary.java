// 檔案: com.farmtastic.proorder.model.FmemOrderSummary.java (更新後)

package com.farmtastic.proorder.model;

import java.sql.Timestamp;
import java.util.Date;

// DTO Projection 介面
public interface FmemOrderSummary {

    // 基礎訂單資訊
    Integer getProOrdId();
    Timestamp getProOrdDate();
    Integer getProOrdGrandTotal();
    Byte getProOrdStatus();
    Byte getProPayStatus();
    
    // 【新增/更新的欄位】
    Byte getProOrdPayment();    // 對應 SQL: PO.PRO_ORD_PAYMENT
    Byte getProOrdShipment();   // 對應 SQL: PO.PRO_ORD_SHIPMENT
    Timestamp getProOrdShipdate(); // 對應 SQL: PO.PRO_ORD_SHIPDATE
    Byte getProOrdAllocStatus();
    Integer getProOrdAllocTotal();

    // 關聯會員資訊
    Integer getMemId(); 
    String getMemName();
}