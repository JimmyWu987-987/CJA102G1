package com.farmtastic.memprocpn.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MemProCpnRepository extends JpaRepository<MemProCpnVO, Integer> {
	// 查某張券的所有持有人
	// 查某會員領過的所有券
	// 查某會員是否已經領過某張券
	// boolean hasCoupon

	// 查某張券「已使用」的紀錄
	List<MemProCpnVO> findUsedRecords(@Param("cpnId") Integer couponId, @Param("status") Byte status);
}
