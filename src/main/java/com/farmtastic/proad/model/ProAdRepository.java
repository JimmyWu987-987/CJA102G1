package com.farmtastic.proad.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProAdRepository extends JpaRepository<ProAdVO, Integer> {

    // =========================================================================
    // 管理員功能
    // =========================================================================

    // 管理員查全部
    List<ProAdVO> findAllByOrderByProAdIdDesc();

    // 管理員查未審核
    List<ProAdVO> findByProAdRevStat(Integer revStat);

    // =========================================================================
    // 小農功能
    // =========================================================================

    // 小農查看廣告列表
    List<ProAdVO> findByFmemIdOrderByProAdIdDesc(Integer fmemId);

    // 小農查看廣告列表(跟產品列表合在一起)
    // @Query("""
    //        select pr
    //          from ProAdVO pr
    //          join pr.product p
    //          join p.fmem f
    //         where f.fmemId = :fmemId
    //         order by pr.proAdId desc
    //        """)
    // List<ProAdVO> findAllByFarmer(@Param("fmemId") Integer fmemId);

    // =========================================================================
    // 消費者顯示 / 共用查詢
    // =========================================================================

    // 活動首頁輪播圖片
    @Query("SELECT p.proAdId " +
           "FROM ProAdVO p " +
           "WHERE p.proAdRevStat = 5 " +
           "  AND p.proAdLaunStat = 1 " +
           "  AND p.proAdStart <= CURRENT_DATE " +
           "  AND p.proAdEnd   >= CURRENT_DATE " +
           "ORDER BY p.proAdStart DESC")
    List<Integer> findPassedAds();

    // 圖片顯示
    @Query("select p.proAdImg from ProAdVO p where p.proAdId = :id")
    byte[] findImgById(@Param("id") Integer id);
}
