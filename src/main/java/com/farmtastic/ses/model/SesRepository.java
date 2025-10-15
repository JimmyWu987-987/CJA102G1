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
}