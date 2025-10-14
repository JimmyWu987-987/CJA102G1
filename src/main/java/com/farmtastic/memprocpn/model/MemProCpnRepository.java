package com.farmtastic.memprocpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmtastic.member.model.Mem;
import com.farmtastic.procpn.model.ProCpnVO;

public interface MemProCpnRepository extends JpaRepository<MemProCpnVO, Integer> {
	// 查某張券的所有持有人
	// 查某會員領過的所有券
	// 查某會員是否已經領過某張券
	// boolean hasCoupon
	// 查「某會員」未使用且有效折價券
	@Query("""
			    SELECT m
			    FROM MemProCpnVO m
			    WHERE m.memVO.memId = :memId
			      AND m.cpnUseStatus = 0
			      AND m.effEnd >= CURRENT_DATE
			""")
	List<MemProCpnVO> findValidCpnByMember(@Param("memId") Integer memId);

	// 查某張券「已使用」的紀錄
	@Query("SELECT m FROM MemProCpnVO m WHERE m.proCpnVO.proCpnId = :cpnId AND m.cpnUseStatus = :status")
	List<MemProCpnVO> findUsedRecords(@Param("cpnId") Integer cpnId, @Param("status") Byte status);

	// 撈該會員的所有折價券（不分狀態）
	@Query("""
			    SELECT m
			    FROM MemProCpnVO m
			    WHERE m.memVO.memId = :memId
			    ORDER BY m.effEnd DESC
			""")
	List<MemProCpnVO> findAllByMember(@Param("memId") Integer memId);

	// 查「某會員＋指定折價券」的資料（用來更新狀態）
	Optional<MemProCpnVO> findByMemVO_MemIdAndProCpnVO_ProCpnId(Integer memId, Integer proCpnId);

	boolean existsByMemVO_MemIdAndProCpnVO_ProCpnId(Integer memId, Integer proCpnId);

	boolean existsByMemVOAndProCpnVO(Mem memVO, ProCpnVO proCpnVO);
}
