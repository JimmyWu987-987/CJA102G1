package com.farmtastic.reg.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.memactcpn.model.MemActCpnVO;
import com.farmtastic.member.model.Mem;
import com.farmtastic.proad.model.ProAdVO;



@Service("regService")
public class RegService {
	@Autowired
	RegRepository repository;
	
	//管理員查全部
	@Transactional(readOnly = true)
	public List<RegVO> getAll() {
		return repository.findAllByOrderByRegIdDesc();
	}
	
	// 管理員顯示小農姓名
	@Transactional(readOnly = true)
	public List<String> getFarmerName() {
		return repository.findAllFarmerNames();
	}
	
	@Transactional(readOnly = true)
	public List<String> getFarmerNameByFmemId(Integer fmemId) {
	    return repository.findFarmerNamesByFmemId(fmemId);
	}
	
	//取得登入後的小農編號 查活動訂單
	@Transactional(readOnly = true)
	public List<RegVO> getByFmemId(Integer fmemId) {
		return repository.findAllByFarmer(fmemId);
	}
	
	//取得登入後的消費者編號 查活動訂單
	@Transactional(readOnly = true)
	public List<RegVO> getByMemId(Integer memId) {
		return repository.findAllByMemIdOrderByRegIdDesc(memId);
	}
	
	
	//取得登入後的消費者報名活動
		@Transactional
		public void addReg(RegVO regVO) {
	    	regVO.setRegAt(Timestamp.from(java.time.Instant.now()));
	    	regVO.setRegStat(0);   	
			repository.save(regVO);
		}
		
		// 更換折價券的寫法
//		// 消費者報名活動時折價卷顯示
//		public List<MemActCpnVO> getCouponsByMemId(Integer memId){
//			return repository.findAvailableCouponsByMemId(memId);
//		}
		
		//消費者報名時顯示會員點數
		public Integer getMemberPoints(Integer memId) {
			return repository.findPointsByMemId(memId);
		}

		
		//小農回覆消費者的留言
		@Transactional
		public void addActCommReply(Integer regId, String fmemReply) {
	        RegVO regVO = repository.findById(regId)
	                .orElseThrow();
	        regVO.setActCommReply(fmemReply);
	        repository.save(regVO);
	    }
		
		
		
		//小農顯示訂單的關聯
		@Transactional(readOnly = true)
		public List<RegExtrasDTO> getActAndSes(Integer fmemId) {
			return repository.findSesTimeAndActName(fmemId);
		}
		
		//消費者顯示訂單的關聯
		@Transactional(readOnly = true)
		public List<RegExtrasDTO> getActAndSesByMemId(Integer memId) {
			return repository.findSesTimeAndActNameByMemId(memId);
		}
	
		
		//消費者報名訂單中的活動名稱
		@Transactional(readOnly = true)
		public SesInfoDTO getSesInfoBySesId(Integer sesId) {
			return repository.findSesInfoBySesId(sesId);
		}
		
		@Transactional(readOnly = true)
		public List<RegVO> getReviewsByActId(Integer actId){
			return repository.findReviewsByActId(actId);
		}
		
		//小農給予評價
		public void addActRate(Integer regId, Integer actRate, String actComm) {
			RegVO regVO = repository.findById(regId)
	                .orElseThrow();
	        regVO.setActRate(actRate);
	        regVO.setActComm(actComm);
	        regVO.setActCommat(Timestamp.from(java.time.Instant.now()));
	        repository.save(regVO);
		}
		
		//小農的所有活動評分
		public List<Integer> getAllRatesByFmemId(Integer fmemId) {
			return repository.findAllRatesByFmemId(fmemId);
		}

		
		
		//管理員查活動完城
		@Transactional(readOnly = true)
		public List<RegVO> findByRevStat(Integer regStat){
			return repository.findByRegStat(regStat);
		}
		
		//改變訂單狀態
		@Transactional
		public void updateRegStat(Integer regId, Integer regStat) {
			RegVO regVO = repository.findById(regId)
	                .orElseThrow();
			regVO.setRegStat(regStat);
			repository.save(regVO);
		}
		
		
		
		// 回傳 RegVO 
	    @Transactional
	    public RegVO addRegAndReturn(RegVO regVO) {
	        regVO.setRegAt(Timestamp.from(java.time.Instant.now()));
	        regVO.setRegStat(0);
	        return repository.save(regVO);
	    }

	    //新增：付款成功更新狀態
	    @Transactional
	    public void updatePayReg(RegVO regVO) {
	        regVO.setRegStat(0); 
	        repository.save(regVO);
	    }
	    
	    //單筆查詢活動訂單
	    @Transactional(readOnly = true)
	    public RegVO getOne(Integer regId) {
	        return repository.findById(regId)
	                         .orElseThrow();
	    }
	    
}
