// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.farmtastic.pro.model;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;


@Repository
public interface ProRepository extends JpaRepository<Pro, Integer>, JpaSpecificationExecutor<Pro> {
	
	@Query("SELECT p FROM Pro p WHERE p.fmemId.fmemId = :fmemId")
	List<Pro> findByFmemId(@Param("fmemId") Integer fmemId);

	
    @Query("SELECT p FROM Pro p LEFT JOIN FETCH p.procateId WHERE p.proId = :proId")
    Optional<Pro> findByIdWithCategory(@Param("proId") Integer proId);
    
    @Query("SELECT p FROM Pro p LEFT JOIN FETCH p.fmemId LEFT JOIN FETCH p.procateId")
    List<Pro> findAllWithDetails();
    
    

}
