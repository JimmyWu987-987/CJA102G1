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
				PO.PRO_ORD_ID,
				PO.PRO_ORD_DATE,
				M.MEM_ID,
				M.MEM_NAME,
				PO.PRO_TOTAL,
				PO.PRO_ORD_GRAND_TOTAL,
				PO.PRO_ORD_COMM,
				PO.PRO_ORD_STATUS,
				PO.PRO_PAY_STATUS,
				PO.PRO_ORD_PAYMENT,
				PO.PRO_ORD_SHIPMENT,
				PO.PRO_ORD_SHIPDATE,
				PO.PRO_ORD_ALLOC_STATUS,
				PO.PRO_ORD_ALLOC_TOTAL,
				PRO_ORD_ALLOC_SEND_FMEM
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
	        ORDER BY PO.PRO_ORD_DATE DESC, PO.PRO_ORD_STATUS DESC
	    """, nativeQuery = true)
	    List<FmemOrderSummary> findFmemProOrders(Integer fmemId);
	
	// 查詢該小農“已到貨”以及“已退貨的”全部訂單，可以撥款的訂單
	@Query(value = """
	        SELECT
				PO.PRO_ORD_ID,
				PO.PRO_ORD_DATE,
				M.MEM_ID,
				M.MEM_NAME,
				PO.PRO_TOTAL,
				PO.PRO_ORD_GRAND_TOTAL,
				PO.PRO_ORD_COMM,
				PO.PRO_ORD_STATUS,
				PO.PRO_PAY_STATUS,
				PO.PRO_ORD_PAYMENT,
				PO.PRO_ORD_SHIPMENT,
				PO.PRO_ORD_SHIPDATE,
				PO.PRO_ORD_ALLOC_STATUS,
				PO.PRO_ORD_ALLOC_TOTAL,
				PRO_ORD_ALLOC_SEND_FMEM
	        FROM
	            product AS P
	        JOIN
	            pro_order_item AS POI ON P.PRO_ID = POI.PRO_ID
	        JOIN
	            pro_order AS PO ON POI.PRO_ORD_ID = PO.PRO_ORD_ID
	        JOIN
	            mem AS M ON PO.MEM_ID = M.MEM_ID
	        WHERE
	            P.FMEM_ID = :fmemId AND (PO.PRO_ORD_STATUS = 3 OR PO.PRO_ORD_STATUS = 6)
	        GROUP BY PO.PRO_ORD_ID
	        ORDER BY PO.PRO_ORD_DATE DESC, PO.PRO_ORD_STATUS DESC
	    """, nativeQuery = true)
	    List<FmemOrderSummary> findFmemProOrdersCanAlloc(Integer fmemId);
	
	
	// 小農查詢該會員有幾筆訂單
	// 小農查詢該商品有幾筆訂單
	// 後台查詢該小農商品有幾筆訂單（回傳多筆）
	// 用復合查詢？
	
}
