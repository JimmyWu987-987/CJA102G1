package com.farmtastic.actcpn.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.farmtastic.common.enums.IsActive;

import jakarta.transaction.Transactional;

public interface ActCpnRepository extends JpaRepository<ActCpnVO, Integer> {
	// 查詢全部啟用或停用的折價券
	List<ActCpnVO> findByIsActive(IsActive isActive);

	// 查詢指定名稱 + 狀態的單張券
	Optional<ActCpnVO> findByCpnNameAndIsActive(String cpnName, IsActive isActive);

	// 名稱模糊查詢
	List<ActCpnVO> findByCpnNameContaining(String keyword);

	// 上架日期篩選
	List<ActCpnVO> findByStartDateAfter(Date startDate);

	List<ActCpnVO> findByStartDateBetween(Date start, Date end);

	// 批次停用過期券
	@Modifying
	@Transactional
	@Query(value = """
			    UPDATE act_cpn
			    SET is_active = 0
			    WHERE DATE_ADD(start_date, INTERVAL valid_days DAY) < CURRENT_DATE()
			      AND is_active = 1
			""", nativeQuery = true)
	void deactivateExpiredCoupons();

	@Query(value = """
			    SELECT *
			    FROM act_cpn
			    WHERE is_active = 1
			      AND CURRENT_DATE BETWEEN start_date AND DATE_ADD(start_date, INTERVAL valid_days DAY)
			""", nativeQuery = true)
	List<ActCpnVO> findAvailableForMember();

}
