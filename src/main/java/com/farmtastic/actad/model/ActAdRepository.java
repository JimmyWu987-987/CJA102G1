package com.farmtastic.actad.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ActAdRepository extends JpaRepository<ActAdVO, Integer>  {

    // ---------------------------------------------------------------------
    // 基本查詢
    // ---------------------------------------------------------------------

    // 管理員查全部
    List<ActAdVO> findAllByOrderByActAdIdDesc();

    // 管理員查未審核
    List<ActAdVO> findByActAdRevStat(Integer revStat);

    // 小農查看廣告列表
    List<ActAdVO> findByFmemIdOrderByActAdIdDesc(Integer fmemId);


    // ---------------------------------------------------------------------
    // 首頁 / 輪播 相關
    // ---------------------------------------------------------------------

    // 活動首頁輪播圖片
    @Query(" SELECT a.actAdId " +
           " FROM ActAdVO a " +
           " WHERE a.actAdRevStat = 5 " +
           "   AND a.actAdLaunStat = 1 " +
           "   AND a.actAdStart <= CURRENT_DATE " +
           "   AND a.actAdEnd   >= CURRENT_DATE " +
           " ORDER BY a.actAdStart DESC ")
    List<Integer> findPassedAds();

    // 活動首頁廣告圖片導入活動頁面
    @Query("select a.actId from ActAdVO a where a.actAdId = :adId")
    Integer findActIdByAdId(@Param("adId") Integer adId);


    // ---------------------------------------------------------------------
    // 其他
    // ---------------------------------------------------------------------

    // 圖片顯示
    @Query("select a.actAdImg from ActAdVO a where a.actAdId = :id")
    byte[] findImgById(@Param("id") Integer id);


    // ---------------------------------------------------------------------
    // 範例（保留原註解，不啟用）
    // ---------------------------------------------------------------------
    // // ● (自訂)條件查詢
    // @Query(value = "from ActAdVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
    // List<ActAdVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
}
