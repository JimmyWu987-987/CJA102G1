// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.farmtastic.act.model;

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
public interface ActRepository extends JpaRepository<Act, Integer> {

    List<Act> findByFmemId(Integer fmemId, Sort sort);
    List<Act> findByFmemId(Integer fmemId);
    List<Act> findByActId(Integer actId);
    List<Act> findByActStat(Integer actStat, Sort sort);
    List<Act> findByActLaunStat(Integer actLaunStat, Sort sort);
    List<Act> findByFmemIdAndActStat(Integer fmemId, Integer actStat, Sort sort);
    
    // 複合查詢 for 小農
    @Query("SELECT DISTINCT a FROM Act a " +
           "JOIN a.actCate ac " +
           "WHERE (:fmemId IS NULL OR a.fmemId = :fmemId) " +
           "AND (:actStat IS NULL OR a.actStat = :actStat) " +
           "AND (:actLaunStat IS NULL OR a.actLaunStat = :actLaunStat) " +
           "AND (:actCateId IS NULL OR ac.actCateId IN :actCateId) " +
           "AND (:keyword IS NULL OR (a.actName LIKE CONCAT('%', :keyword, '%') " +
           "OR a.actDes LIKE CONCAT('%', :keyword, '%')))")
    List<Act> findActByCQForFmem(@Param("fmemId") Integer fmemId,
    							 @Param("actStat") Integer actStat,
    							 @Param("actLaunStat") Integer actLaunStat,
    							 @Param("actCateId") List<Integer> actCateId,
    							 @Param("keyword") String keyword,
    							 Sort sort);
    
    // 複合查詢 for 消費者 (不篩小農)
    @Query("SELECT DISTINCT a FROM Act a " +
           "JOIN a.actCate ac " +
           "JOIN a.fmem f " +
           "WHERE a.actLaunStat = 1 " + 
           "AND (:actCateId IS NULL OR ac.actCateId IN :actCateId) " +
           "AND (:keyword IS NULL OR (a.actName LIKE CONCAT('%', :keyword, '%') " +
           "OR a.actDes LIKE CONCAT('%', :keyword, '%') " +
           "OR f.storeName LIKE CONCAT('%', :keyword, '%')))")
     List<Act> findActByCQForCus(@Param("actCateId") List<Integer> actCateId,
    		 					 @Param("keyword") String keyword,
    		 					 Sort sort);
    
    // 複合查詢 for 後台 (跟小農差不多, 不過也能透過小農的商店名稱關鍵字查詢)
    @Query("SELECT DISTINCT a FROM Act a " +
           "JOIN a.actCate ac " +
           "JOIN a.fmem f " +
           "WHERE (:fmemId IS NULL OR a.fmemId = :fmemId) " +
           "AND (:actStat IS NULL OR a.actStat = :actStat) " +
           "AND (:actLaunStat IS NULL OR a.actLaunStat = :actLaunStat) " +
           "AND (:actCateId IS NULL OR ac.actCateId IN :actCateId) " +
           "AND (:keyword IS NULL OR (a.actName LIKE CONCAT('%', :keyword, '%') " +
           "OR a.actDes LIKE CONCAT('%', :keyword, '%') " +
           "OR f.storeName LIKE CONCAT('%', :keyword, '%')))")
    List<Act> findActByForAdmin(@Param("fmemId") Integer fmemId,
    							@Param("actStat") Integer actStat,
    							@Param("actLaunStat") Integer actLaunStat,
    							@Param("actCateId") List<Integer> actCateId,
    							@Param("keyword") String keyword,
    							Sort sort);
    
    
    @Query("SELECT a FROM Act a LEFT JOIN FETCH a.actImg WHERE a.actId = :actId")
    Optional<Act> findByActIdWithImgs(@Param("actId") Integer actId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Act a WHERE a.actId = :actId")
    void deleteByActId(@Param("actId") Integer actId);
}