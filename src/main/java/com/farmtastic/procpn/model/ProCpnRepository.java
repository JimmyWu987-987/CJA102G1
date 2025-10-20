package com.farmtastic.procpn.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.farmtastic.common.enums.IsActive;

import jakarta.transaction.Transactional;

public interface ProCpnRepository extends JpaRepository<ProCpnVO, Integer> {

	// 查詢全部啟用或停用的折價券
	List<ProCpnVO> findByIsActive(IsActive isActive);

	// 查詢指定名稱單張券
	Optional<ProCpnVO> findByCpnName(String cpnName);

	// 查詢指定名稱 + 狀態的單張券
	Optional<ProCpnVO> findByCpnNameAndIsActive(String cpnName, IsActive isActive);

	// 名稱模糊查詢
	List<ProCpnVO> findByCpnNameContaining(String keyword);

	// 折扣類型查詢
//	List<ProCpnVO> findByDiscType(DiscountType discType);

	// 上架日期篩選
	Page<ProCpnVO> findByStartDateBetween(Date start, Date end, Pageable pageable);

	Page<ProCpnVO> findByStartDateAfter(Date start, Pageable pageable);

	Page<ProCpnVO> findByStartDateBefore(Date end, Pageable pageable);

	// 批次停用過期券
	@Modifying
	@Transactional
	@Query(value = """
			    UPDATE pro_cpn
			    SET is_active = 0
			    WHERE DATE_ADD(start_date, INTERVAL valid_days DAY) < CURRENT_DATE()
			      AND is_active = 1
			""", nativeQuery = true)
	void deactivateExpiredCoupons();

	@Query(value = """
			    SELECT *
			    FROM pro_cpn
			    WHERE is_active = 1
			      AND CURRENT_DATE BETWEEN start_date AND DATE_ADD(start_date, INTERVAL valid_days DAY)
			""", nativeQuery = true)
	List<ProCpnVO> findAvailableForMember();

}
