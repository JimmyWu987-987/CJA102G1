package com.farmtastic.proorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.farmtastic.member.model.Mem;

@Repository
public interface ProOrderRepository extends JpaRepository<ProOrderVO,Integer>{
	
	// 一般會員查自己的全部訂單
	List<ProOrderVO> findByMemVO(Mem memVO);
	
	// 小農查詢自己的全部表單
	@Query(value = """
	        SELECT
	            PO.PRO_ORD_ID AS proOrdId,
	            PO.PRO_ORD_DATE AS proOrdDate,
	            M.MEM_ID AS memId,
	            M.MEM_NAME AS memName,
	            PO.PRO_ORD_GRAND_TOTAL AS proOrdGrandTotal,
	            PO.PRO_ORD_STATUS AS proOrdStatus,
	            PO.PRO_PAY_STATUS AS proPayStatus,
	            PO.PRO_ORD_PAYMENT AS proOrdPayment,    -- 新增欄位
	            PO.PRO_ORD_SHIPMENT AS proOrdShipment,  -- 新增欄位
	            PO.PRO_ORD_SHIPDATE AS proOrdShipdate   -- 新增欄位
	        FROM
	            product AS P
	        JOIN
	            pro_order_item AS POI ON P.PRO_ID = POI.PRO_ID
	        JOIN
	            pro_order AS PO ON POI.PRO_ORD_ID = PO.PRO_ORD_ID
	        JOIN
	            mem AS M ON PO.MEM_ID = M.MEM_ID
	        WHERE
	            P.FMEM_ID = :fmemId
	        GROUP BY PO.PRO_ORD_ID
	        ORDER BY PO.PRO_ORD_DATE DESC
	    """, nativeQuery = true)
	    List<FmemOrderSummary> findFmemProOrders(Integer fmemId);
	
	// 小農查詢該會員有幾筆訂單
	// 小農查詢該商品有幾筆訂單
	// 後台查詢該小農商品有幾筆訂單（回傳多筆）
	// 用復合查詢？
	
}
