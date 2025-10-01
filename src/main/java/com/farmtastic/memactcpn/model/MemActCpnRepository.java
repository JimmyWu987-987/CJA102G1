package com.farmtastic.memactcpn.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmtastic.common.enums.CpnUseStatus;

public interface MemActCpnRepository extends JpaRepository<MemActCpnVO, Integer> {
	// 查某張券的所有持有人
	// 查某會員領過的所有券
	// 查某會員是否已經領過某張券
	// boolean hasCoupon

	// 查某張券「已使用」的紀錄
	@Query("SELECT m FROM MemActCpnVO m WHERE m.actCpnVO.actCpnId = :cpnId AND m.cpnUseStatus = :status")
	List<MemActCpnVO> findUsedRecords(@Param("cpnId") Integer couponId, @Param("status") CpnUseStatus status);
}
