package com.farmtastic.fmember.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FmemRepository extends JpaRepository<Fmem, Integer>{

	Fmem findByFmemAcc(String fmemAcc);
	
	Fmem findByFmemMobile(String fmemMobile);
	
	@Query("select f from Fmem f where f.fId = :fId")
	Fmem findByFid(@Param("fId") String fId);
	
//	@Query("SELECT f FROM Fmem f WHERE f.fId = :fId")  // ⭐ 使用 @Query 明確指定
//    Fmem findByFid(@Param("fId") String fId);
	
	//台灣地圖需要
	@Query("select f from Fmem f where f.fmemCity  = :fmemCity and accStatus=2")
	List<Fmem> findByFmemCity(String fmemCity);
	

	
}
