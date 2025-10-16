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
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProRepository;



@Service("proAdService")
public class ProAdService {

	@Autowired
	ProAdRepository repository;
	
//---------------管理員功能---------------
	//管理員查全部
	@Transactional(readOnly = true)
	public List<ProAdVO> getAll() {
		return repository.findAllByOrderByProAdIdDesc();
	}
	
	//管理員查未審核
	@Transactional(readOnly = true)
	public List<ProAdVO> findByRevStat(Integer revStat){
		return repository.findByProAdRevStat(revStat);
	}
	
	//管理員查單一是否存在
	@Transactional(readOnly = true)
  	public boolean exists(Integer id) {
  	    return repository.existsById(id);
  	}
	
    //管理員根據廣告編號查單一
  	@Transactional(readOnly = true)
    public ProAdVO getOneProAd(Integer proAdId) {
    	if (proAdId == null) return null;
		Optional<ProAdVO> optional = repository.findById(proAdId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
    
    
    //管理員審核頁面(審核通過，繳費期限加七天)
    @Transactional
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
    
  //管理員修改資料
    @Transactional
	public void updateProAd(Integer proAdId ,byte[] adImg, Integer proAdRevStat, String proAdRemark, Integer proAdLaunStat,
			Date proAdStart,Date proAdEnd,Integer proAdFee, Date proAdFeeEnd) {
		ProAdVO proAdVO = repository.getById(proAdId);
		proAdVO.setProAdImg(adImg);
		proAdVO.setProAdRevStat(proAdRevStat);
		proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdRevRemark(proAdRemark);
		proAdVO.setProAdLaunStat(proAdLaunStat);
		proAdVO.setProAdLaunUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdStart(proAdStart);
		proAdVO.setProAdEnd(proAdEnd);
		proAdVO.setProAdFee(proAdFee);
		proAdVO.setProAdFeeEnd(proAdFeeEnd);
		repository.save(proAdVO);
	}
    
   
	
    
//---------------小農功能---------------
    //取得登入後的小農編號(小農查自己的商品廣告)
    @Transactional(readOnly = true)
	public List<ProAdVO> findByFmemId(Integer fmemId) {
    	return repository.findByFmemIdOrderByProAdIdDesc(fmemId);
	}
    
  //小農申請商品廣告(申請完後進入待審核狀態及下架中狀態)
	@Transactional
    public void addProAd(ProAdVO proAdVO) {
    	proAdVO.setProAdRevStat(1);
    	proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
    	proAdVO.setProAdRevRemark("待審核");
    	proAdVO.setProAdLaunStat(0);
    	proAdVO.setProAdLaunUpd(Timestamp.from(java.time.Instant.now()));
		repository.save(proAdVO);
	}
    
	//小農繳完商品廣告費(繳完費後進入已繳費及上架中狀態) (上架中狀態要寫排成器,暫時寫死)
    @Transactional
	public void updatePayAd(ProAdVO proAdVO) {
    	proAdVO.setProAdRevStat(5);
		proAdVO.setProAdRevUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdRevRemark("已繳費");
		proAdVO.setProAdLaunStat(1);
		proAdVO.setProAdLaunUpd(Timestamp.from(java.time.Instant.now()));
		proAdVO.setProAdFeeEnd(null);
		
	}
	
    
    
    
    
    
    
  //---------------相關功能---------------
  //圖片顯示
	@Transactional(readOnly = true)
    public byte[] getImgBytes(Integer id) {
        return repository.findImgById(id);
    }

    
  //拿小農會員的關聯
    @Autowired FmemRepository fmemRepository;
  
    @Transactional(readOnly = true)
    public Fmem getFmemRef(Integer fmemId) {
        return fmemRepository.getReferenceById(fmemId);
    }
    
  //拿商品的關聯
    @Autowired
    private ProRepository productRepository;
    @Transactional(readOnly = true)
    public List<Pro> findFmemProducts(Integer fmemId) {
    	   return productRepository.findByFmemId(fmemId);
    }

    @Transactional(readOnly = true)
    public Pro getProductRef(Integer proId) {
        return productRepository.getReferenceById(proId);
    }
    
    @Transactional(readOnly = true)
    // 取得條件符合的商品廣告
    public List<Integer> getPassProAds() {
    	return repository.findPassedAds();
    }
    
   
}
