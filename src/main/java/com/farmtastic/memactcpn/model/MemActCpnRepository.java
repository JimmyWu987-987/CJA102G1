package com.farmtastic.memactcpn.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.member.model.Mem;

public interface MemActCpnRepository extends JpaRepository<MemActCpnVO, Integer> {
	// 查某張券的所有持有人
	// 查某會員領過的所有券
	// 查某會員是否已經領過某張券
	// boolean hasCoupon
	// 查「某會員」未使用且有效折價券
	@Query("""
			    SELECT m
			    FROM MemActCpnVO m
			    WHERE m.memVO.memId = :memId
			      AND m.cpnUseStatus = 0
			      AND m.effEnd >= CURRENT_DATE
			""")
	List<MemActCpnVO> findValidCpnByMember(@Param("memId") Integer memId);

	// 查某張券「已使用」的紀錄
	@Query("SELECT m FROM MemActCpnVO m WHERE m.actCpnVO.actCpnId = :cpnId AND m.cpnUseStatus = :status")
	List<MemActCpnVO> findUsedRecords(@Param("cpnId") Integer cpnId, @Param("status") Byte status);

	// 撈該會員的所有折價券（不分狀態）
	@Query("""
			    SELECT m
			    FROM MemActCpnVO m
			    JOIN FETCH m.actCpnVO
			    WHERE m.memVO.memId = :memId
			    ORDER BY m.effEnd DESC
			""")
	List<MemActCpnVO> findAllByMember(@Param("memId") Integer memId);

	// 查「某會員＋指定折價券」的資料（用來更新狀態）
	Optional<MemActCpnVO> findByMemVO_MemIdAndActCpnVO_ActCpnId(Integer memId, Integer proCpnId);

	boolean existsByMemVO_MemIdAndActCpnVO_ActCpnId(Integer memId, Integer proCpnId);

	boolean existsByMemVOAndActCpnVO(Mem memVO, ActCpnVO actCpnVO);

	// 查某張券「已使用」的紀錄
	@Query("SELECT m FROM MemActCpnVO m WHERE m.actCpnVO.actCpnId = :cpnId AND m.cpnUseStatus = :status")
	List<MemActCpnVO> findUsedRecords(@Param("cpnId") Integer couponId, @Param("status") CpnUseStatus status);

	@Query("""
			SELECT mac FROM MemActCpnVO mac
			JOIN mac.memVO mem
			JOIN mac.actCpnVO cpn
			WHERE LOWER(mem.memAcc) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(cpn.cpnName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			ORDER BY mac.rcvAt DESC
			""")
	List<MemActCpnVO> searchByKeyword(@Param("keyword") String keyword);
}
