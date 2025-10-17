package com.farmtastic.reg.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.memactcpn.model.MemActCpnVO;

public interface RegRepository extends JpaRepository<RegVO, Integer>{
	// 管理員查詢活動訂單全部
	List<RegVO> findAllByOrderByRegIdDesc();
	
	// 管理員查未審核
	List<RegVO> findByRegStat(Integer regStat);
	
	//小農查詢活動訂單
	 @Query(value = """
		      select r.*
		      from reg r
		      join ses s on s.ses_id = r.ses_id
		      join act a on a.act_id = s.act_id
		      where a.fmem_id = :fmemId
		      order by r.reg_id desc
		      """, nativeQuery = true)
	List<RegVO> findAllByFarmer(@Param("fmemId") Integer fmemId);
	 
	// 消費者查詢活動訂單
	    List<RegVO> findAllByMemIdOrderByRegIdDesc(Integer memId);
	    
	 // 更換折價券的寫法
	// 消費者報名活動時折價卷顯示
//	    @Query("SELECT m FROM MemActCpnVO m WHERE m.memVO.memId = :memId AND m.usedAt IS NULL AND m.effEnd >= CURRENT_DATE")
//	    List<MemActCpnVO> findAvailableCouponsByMemId(@Param("memId") Integer memId);

	// 消費者報名活動時點數顯示
	    @Query("SELECT m.memPoint FROM Mem m WHERE m.memId = :memId") 
	    Integer findPointsByMemId(@Param("memId") Integer memId);
	    
	    
	 // 小農取得活動名稱、場次日期、場次時間
	    @Query("""
	      select new com.farmtastic.reg.model.RegExtrasDTO(
	               r.regId,
	               s.sesDate,
	               s.sesStart,
	               s.sesEnd,
	               a.actName
	             )
	        from RegVO r
	          join Ses s on s.sesId = r.sesId
	          join Act a on a.actId = s.actId
	       where a.fmemId = :fmemId
	       order by r.regId desc
	    """)
	    List<RegExtrasDTO> findSesTimeAndActName(@Param("fmemId") Integer fmemId);


	    
	    
	    // 消費者取得活動名稱、場次日期、場次時間
	    @Query("""
	            select new com.farmtastic.reg.model.RegExtrasDTO(
	                r.regId,
	                s.sesDate,
	                s.sesStart,
	                s.sesEnd,
	                a.actName
	            )
	             from RegVO r
	    		  join Ses s on s.sesId = r.sesId
	    		  join Act a on a.actId = s.actId
	            where r.memId = :memId
	            order by r.regId desc
	        """)
	        List<RegExtrasDTO> findSesTimeAndActNameByMemId(@Param("memId") Integer memId);
	    
	    // 報名頁面拿活動名稱與場次時間
	    @Query("""
	      select new com.farmtastic.reg.model.SesInfoDTO(
	        s.sesId, a.actId,a.actName, s.sesDate, s.sesStart, s.sesEnd, s.sesFee
	      )
	      from Ses s join s.act a
	      where s.sesId = :sesId
	    """)
	    SesInfoDTO findSesInfoBySesId(@Param("sesId") Integer sesId);

	    
	 // 評價時間 + 消費者評分 + 評論 + 小農回覆
	    @Query("""
	      select r
	      from RegVO r
	        join Ses s on s.sesId = r.sesId
	        join Act a on a.actId = s.actId
	      where a.actId = :actId
	        and (r.actRate is not null or r.actComm is not null or r.actCommReply is not null)
	      order by
	        r.actCommat desc
	    """)
	    List<RegVO> findReviewsByActId(@Param("actId") Integer actId);
	    
	    
}


