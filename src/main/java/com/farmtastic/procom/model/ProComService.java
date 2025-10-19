package com.farmtastic.procom.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procom.dto.ProComByFmemIdDTO;

@Service
public class ProComService {

	@Autowired
	ProComRepository repository;

	@Autowired
	ProService proSvc;

	// 查詢該商品的所有評論
	public List<ProComVO> getProComByProVO(Pro proVO) {
		return repository.findByProVO(proVO);
	}

	// 查詢該會員的所有評論
	public List<ProComVO> getProComByMemVO(Mem memVO) {
		return repository.findByMemVO(memVO);
	}

	// 查詢該小農的所有評論(依照時間排序)
	public List<ProComByFmemIdDTO> getProComByFmemId(Integer fmemId) {
		return repository.findProComByFmemId(fmemId);
	}

	// 計算該商品的總分數
	public Integer countProComRateByProId(Integer proId) {
		Pro proVO = proSvc.getOnePro(proId);
		List<ProComVO> proComList = getProComByProVO(proVO);

		int countRate = 0;

		for (ProComVO proComVO : proComList) {
			countRate += proComVO.getProComRate();
		}

		return countRate;
	}

	// 計算該小農所有商品評價的總分數
	public Integer countProComRateByFmemId(Integer fmemId) {
		List<Pro> ProList = proSvc.findByFmemId(fmemId);

		int totalRate = 0;

		for (Pro proVo : ProList) {
			int proRate = countProComRateByProId(proVo.getProId());
			totalRate += proRate;
		}

		return totalRate;
	}
	// 單一小農的總評倫數

	// 計算該小農的總評論數
	public Integer countProComByFmemId(Integer fmemId) {
		List<Pro> fmemProList = proSvc.findByFmemId(fmemId);

		int countProCom = 0;
		
		if(fmemProList == null || fmemProList.isEmpty()) {
			return 0;
		}

		for (Pro proVO : fmemProList) {

			List<ProComVO> proComList = getProComByProVO(proVO);

			if (proComList != null) {
				countProCom += proComList.size();
			}
		}
		return countProCom;
	}

	// =============== 基礎功能 ===============

	// 新增
	public void addProCom(ProComVO proComVO) {
		repository.save(proComVO);
	}

	// 修改
	public void updateProCom(ProComVO proComVO) {
		repository.save(proComVO);
	}

	// 刪除
	public void deleteProCom(Integer proComId) {
		if (repository.existsById(proComId))
			repository.deleteById(proComId);
	}

	// 查全部
	public List<ProComVO> getAll() {
		return repository.findAll();
	}

	// 查單一
	public ProComVO getOneProCom(Integer proComId) {
		Optional<ProComVO> optional = repository.findById(proComId);
		return optional.orElse(null);
	}

}
