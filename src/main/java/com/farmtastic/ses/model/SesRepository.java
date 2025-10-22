// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.farmtastic.ses.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesRepository extends JpaRepository<Ses, Integer> {

	List<Ses> findByActId(Integer actId, Sort sort);

	List<Ses> findByAct_Fmem_FmemId(Integer fmemId, Sort sort);

	Optional<Ses> findBySesId(Integer sesId);

	List<Ses> findBySesLaunStat(Integer sesLaunStat, Sort sort);

	List<Ses> findByRegStat(Integer regStat, Sort sort);

	@Transactional
	@Modifying
	@Query("DELETE FROM Ses a WHERE a.actId = :sesId")
	void deleteBySesId(@Param("sesId") Integer sesId);

	// 報名人數應為正常訂單狀態 (0已成立. 3已完成. 4待撥款. 5已完成) 的人數加總
	@Query("SELECT SUM(r.regCount) FROM RegVO r WHERE r.sesId = :sesId AND r.regStat IN (0, 3, 4, 5)")
	Integer getHeadCountBySesId(@Param("sesId") Integer sesId);

	// 用另一個方式
	@Query("SELECT s FROM Ses s JOIN FETCH s.act a WHERE a.fmem.fmemId = :fmemId")
	List<Ses> findSesWithActByFmemId(@Param("fmemId") Integer fmemId, Sort sort);

}