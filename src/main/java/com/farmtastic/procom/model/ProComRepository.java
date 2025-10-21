package com.farmtastic.procom.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.LowRatePro;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.procom.dto.ProComByFmemIdDTO;

@Repository
public interface ProComRepository extends JpaRepository<ProComVO, Integer> {
	
	// 查詢該商品的所有評論
	List<ProComVO> findByProVO(Pro proVO);
	
	// 查詢該會員的所有評論
	List<ProComVO> findByMemVO(Mem memVO);
	
	// 計算該商品的總分數
	// 功能在 ProComService.java 內 
	
	// 查詢該小農的所有評論(依照時間排序)
	// 功能在 ProComService.java 內
	@Query(value = """
	        SELECT
				f.fmem_id,
                pcom.pro_com_id,
				pcom.pro_id,
				pcom.mem_id,
				pcom.pro_com_content,
				pcom.pro_com_time,
				pcom.pro_com_rate
	        FROM
	            fmem AS f
	        JOIN
	            product AS pro ON f.fmem_id = pro.fmem_id
	        JOIN
	            pro_com AS pcom ON pro.pro_id = pcom.pro_id
	        WHERE
	            f.fmem_id = :fmemId
			order by
				pcom.pro_com_time
	    """, nativeQuery = true)
	List<ProComByFmemIdDTO> findProComByFmemId(Integer fmemId);
	

	// 新增，計算每個商品的平均評分，並篩選出平均小於 2 的商品
    @Query("SELECT new com.farmtastic.pro.model.LowRatePro(" +
            "p.proId, p.fmemId.fmemId, CAST(AVG(pc.proComRate) AS double), p.proStatus) " +
            "FROM ProComVO pc JOIN pc.proVO p " +  
            "GROUP BY p.proId, p.fmemId.fmemId, p.proStatus " +
            "HAVING AVG(pc.proComRate) < 3.0")
     List<LowRatePro> findProductsWithAverageRatingLessThan();
    
    //新增：只查詢未下架的低評分商品（proStatus = 1）
    @Query("SELECT new com.farmtastic.pro.model.LowRatePro(" +
            "p.proId, p.fmemId.fmemId, CAST(AVG(pc.proComRate) AS double), p.proStatus) " +
            "FROM ProComVO pc JOIN pc.proVO p " + 
            "WHERE p.proStatus = 1 " +
            "GROUP BY p.proId, p.fmemId.fmemId, p.proStatus " +
            "HAVING AVG(pc.proComRate) < 3.0")
    List<LowRatePro> findActiveProductsWithLowRating();
	
}
