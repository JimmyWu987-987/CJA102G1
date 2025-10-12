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

@Repository
public interface ActRepository extends JpaRepository<Act, Integer> {

    List<Act> findByFmemId(Integer fmemId, Sort sort);
    List<Act> findByActId(Integer actId);
    List<Act> findByActStat(Integer actStat, Sort sort);
    List<Act> findByActLaunStat(Integer actLaunStat, Sort sort);
    List<Act> findByFmemIdAndActStat(Integer fmemId, Integer actStat, Sort sort);

    // 複合查詢(用Repository + Sort/JPQL), 練習不用原生sql...
    @Query("SELECT DISTINCT a FROM act a " +
           "JOIN a.actCate ac " +
           "WHERE (:fmemId IS NULL OR a.fmemId = :fmemId) " +
           "AND (:actStat IS NULL OR a.actStat = :actStat) " +
           "AND (:actLaunStat IS NULL OR a.actLaunStat = :actLaunStat) " +
           "AND (:actcateId IS NULL OR ac.actCateId = :actcateId) " +
           "AND (:keyword IS NULL OR a.actName LIKE CONCAT('%', :keyword, '%') " +
           "OR a.actDes LIKE CONCAT('%', :keyword, '%'))")
    List<Act> findActByCQ(@Param("fmemId") Integer fmemId,
                               @Param("actStat") Integer actStat,
                               @Param("actLaunStat") Integer actLaunStat,
                               @Param("actcateId") Integer actcateId,
                               @Param("keyword") String keyword,
                               Sort sort);

    @Transactional
    @Modifying
    @Query("DELETE FROM act a WHERE a.actId = :actId")
    void deleteByActId(@Param("actId") Integer actId);
}