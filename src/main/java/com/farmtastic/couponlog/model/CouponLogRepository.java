package com.farmtastic.couponlog.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponLogRepository extends JpaRepository<CouponLogVO, Integer> {

	// 該管理員的折價券操作紀錄清單（依時間降冪排序）
	@Query("SELECT c FROM CouponLogVO c WHERE c.adminId = :adminId ORDER BY c.actionTime DESC")
	List<CouponLogVO> findByAdminId(Integer adminId);

	// 該折價券的操作紀錄（依時間降冪排序）
	@Query("SELECT c FROM CouponLogVO c WHERE c.proCpnId = :proCpnId ORDER BY c.actionTime DESC")
	List<CouponLogVO> findByProCpnId(Integer proCpnId);

	// 全部操作日誌清單（時間新 → 舊）
	@Query("SELECT c FROM CouponLogVO c ORDER BY c.actionTime DESC")
	List<CouponLogVO> findAllOrderByTimeDesc();

}
