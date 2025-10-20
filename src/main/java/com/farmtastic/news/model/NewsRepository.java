package com.farmtastic.news.model;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{

    List<News> findByNewsStatusOrderByNewsAtDesc(Integer newsStatus);
    
    @Query("SELECT n FROM News n WHERE n.newsStatus = 0 AND (n.fmem IS NULL OR n.fmem.fmemId = :fmemId)")
    List<News> findNewsForFarmer(@Param("fmemId") Integer fmemId, Sort sort);
}
