package com.farmtastic.proad.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemRepository;



@Service("proAdService")
public class ProAdService {

	@Autowired
	ProAdRepository repository;
	
	//管理員查全部
	public List<ProAdVO> getAll() {
		return repository.findAllByOrderByProAdIdDesc();
	}
	
	//管理員查未審核
	public List<ProAdVO> findByRevStat(Integer revStat){
		return repository.findByProAdRevStat(revStat);
	};
	
	//管理員查單一是否存在
  	public boolean exists(Integer id) {
  	    return repository.existsById(id);
  	}
	
    //管理員根據廣告編號查單一
    public ProAdVO getOneProAd(Integer proAdId) {
    	if (proAdId == null) return null;
		Optional<ProAdVO> optional = repository.findById(proAdId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
    
    
    //管理員審核頁面
    public void updateStatus(Integer proAdId, Integer revStat, String remark) {
        // 取得該廣告
        ProAdVO proAdVO = repository.getById(proAdId);
        proAdVO.setProAdRevStat(revStat);
        proAdVO.setProAdRevRemark(remark);
        proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
        if(revStat == 4) {
        	proAdVO.setProAdFeeEnd((java.sql.Date.valueOf(LocalDate.now().plusDays(7))));
        }
        repository.save(proAdVO);
    }
    
    //取得登入後的小農編號
	public void findByfmemId(Integer fmemId) {
		repository.findByFmemFmemId(fmemId);
	}
	
    //小農申請商品廣告
    public void addProAd(ProAdVO proAdVO) {
    	proAdVO.setProAdRevStat(1);
    	proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
    	proAdVO.setProAdRevRemark("待審核");
    	proAdVO.setProAdLaunStat(0);
    	proAdVO.setProAdLaunUpd(Timestamp.from(java.time.Instant.now()));
		repository.save(proAdVO);
	}
    
    //管理員修改資料
	public void updateProAd(Integer proAdId ,byte[] adImg, Integer proAdRevStat, String proAdRemark, Integer proAdLaunStat,
			Integer proAdFee, Date proAdFeeEnd) {
		ProAdVO proAdVO = repository.getById(proAdId);
		proAdVO.setProAdImg(adImg);
		proAdVO.setProAdRevStat(proAdRevStat);
		proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdRevRemark(proAdRemark);
		proAdVO.setProAdLaunStat(proAdLaunStat);
		proAdVO.setProAdLaunUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdFee(proAdFee);
		proAdVO.setProAdFeeEnd(proAdFeeEnd);
		repository.save(proAdVO);
	}
	
  //圖片顯示
    @Transactional
    public byte[] getImgBytes(Integer id) {
        return repository.findImgById(id);
    }

    
  //拿小農會員的關聯
    @Autowired FmemRepository fmemRepository;

    @Transactional(readOnly = true)
    public Fmem getFmemRef(Integer fmemId) {
        return fmemRepository.getReferenceById(fmemId);
    }
}
