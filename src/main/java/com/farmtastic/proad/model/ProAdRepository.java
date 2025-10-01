package com.farmtastic.proad.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ProAdRepository extends JpaRepository<ProAdVO, Integer> {
	//管理員查全部
	List<ProAdVO> findAllByOrderByProAdIdDesc();
	//管理員查未審核
	List<ProAdVO> findByProAdRevStat(Integer revStat); 
	//小農申請活動廣告
	List<ProAdVO> findByFmemFmemId(Integer fmemId);
	
	
	@Query("select p.proAdImg from ProAdVO p where p.proAdId = :id")
	byte[] findImgById(@Param("id") Integer id);
}
