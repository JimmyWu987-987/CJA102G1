package com.farmtastic.actad.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActRepository;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemRepository;

@Service("actAdService")
public class ActAdService {

    @Autowired
    ActAdRepository repository;

    // -------------------------------------------------------------------------
    // 管理員功能
    // -------------------------------------------------------------------------

    // 管理員查全部
    @Transactional(readOnly = true)
    public List<ActAdVO> getAll() {
        return repository.findAllByOrderByActAdIdDesc();
    }

    // 管理員查未審核
    @Transactional(readOnly = true)
    public List<ActAdVO> findByRevStat(Integer revStat) {
        return repository.findByActAdRevStat(revStat);
    }

    // 管理員查單一是否存在
    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        return repository.existsById(id);
    }

    // 管理員根據廣告編號查單一
    public ActAdVO getOneActAd(Integer actAdId) {
        if (actAdId == null) return null;
        Optional<ActAdVO> optional = repository.findById(actAdId);
//      return optional.get();
        return optional.orElse(null);
    }

    // 管理員審核頁面(審核通過，繳費期限加七天)
    @Transactional
    public void updateStatus(Integer actAdId, Integer revStat, String remark) {
        // 取得該廣告
        ActAdVO actAdVO = repository.findById(actAdId).orElseThrow();
        actAdVO.setActAdRevStat(revStat);
        actAdVO.setActAdRevRemark(remark);
        actAdVO.setActAdRevUpd(Timestamp.from(java.time.Instant.now()));
        if (revStat == 4) {
            actAdVO.setActAdFeeEnd((java.sql.Date.valueOf(LocalDate.now().plusDays(7))));
        }
        repository.save(actAdVO);
    }

    // 管理員修改資料
    @Transactional
    public void updateActAd(Integer actAdId, byte[] adImg, Integer actAdRevStat, String actAdRemark, Integer actAdLaunStat,
                            Date actAdStart, Date actAdEnd, Integer actAdFee, Date actAdFeeEnd) {
        ActAdVO actAdVO = repository.findById(actAdId).orElseThrow();
        actAdVO.setActAdImg(adImg);
        actAdVO.setActAdRevStat(actAdRevStat);
        actAdVO.setActAdRevUpd(Timestamp.from(java.time.Instant.now()));
        actAdVO.setActAdRevRemark(actAdRemark);
        actAdVO.setActAdLaunStat(actAdLaunStat);
        actAdVO.setActAdLaunUpd(Timestamp.from(java.time.Instant.now()));
        actAdVO.setActAdStart(actAdStart);
        actAdVO.setActAdEnd(actAdEnd);
        actAdVO.setActAdFee(actAdFee);
        actAdVO.setActAdFeeEnd(actAdFeeEnd);
        repository.save(actAdVO);
    }

    // -------------------------------------------------------------------------
    // 小農功能
    // -------------------------------------------------------------------------

    // 取得登入後的小農編號(小農查自己的商品廣告)
    @Transactional(readOnly = true)
    public List<ActAdVO> findByFmemId(Integer fmemId) {
        return repository.findByFmemIdOrderByActAdIdDesc(fmemId);
    }

    // 小農申請商品廣告(申請完後進入待審核狀態及下架中狀態)
    @Transactional
    public void addActAd(ActAdVO actAdVO) {
        actAdVO.setActAdRevStat(1);
        actAdVO.setActAdRevUpd(Timestamp.from(java.time.Instant.now()));
        actAdVO.setActAdRevRemark("待審核");
        actAdVO.setActAdLaunStat(0);
        actAdVO.setActAdLaunUpd(Timestamp.from(java.time.Instant.now()));
        repository.save(actAdVO);
    }

    // 小農繳完商品廣告費(繳完費後進入已繳費及上架中狀態) (上架中狀態要寫排成器,暫時寫死)
    @Transactional
    public void updatePayAd(ActAdVO actAdVO) {
        actAdVO.setActAdRevStat(5);
        actAdVO.setActAdRevUpd(Timestamp.from(java.time.Instant.now()));
        actAdVO.setActAdRevRemark("已繳費");
        actAdVO.setActAdLaunStat(1);
        actAdVO.setActAdLaunUpd(Timestamp.from(java.time.Instant.now()));
        actAdVO.setActAdFeeEnd(null);
    }

    // -------------------------------------------------------------------------
    // 相關功能
    // -------------------------------------------------------------------------

    // 圖片顯示
    @Transactional(readOnly = true)
    public byte[] getImgBytes(Integer id) {
        return repository.findImgById(id);
    }

    // 拿小農會員的關聯
    @Autowired
    FmemRepository fmemRepository;

    @Transactional(readOnly = true)
    public Fmem getFmemRef(Integer fmemId) {
        return fmemRepository.getReferenceById(fmemId);
    }

    // 拿活動的關聯
    @Autowired
    private ActRepository actRepository;

    @Transactional(readOnly = true)
    public List<Act> findFmemAct(Integer fmemId) {
        return actRepository.findByFmemId(fmemId);
    }

    @Transactional(readOnly = true)
    public Act getActRef(Integer actId) {
        return actRepository.getReferenceById(actId);
    }

    @Transactional(readOnly = true)
    // 取得條件符合的活動廣告
    public List<Integer> getPassActAds() {
        return repository.findPassedAds();
    }

    // 活動首頁廣告圖片導入活動頁面
    public Integer findActIdByAdId(Integer adId) {
        return repository.findActIdByAdId(adId);
    }
}
