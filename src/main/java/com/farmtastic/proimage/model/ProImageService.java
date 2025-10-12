package com.farmtastic.proimage.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.proimage.model.NotFoundException;


	@Service
	public class ProImageService {

	@Autowired
	private ProImageRepository proImageRepository;

	public ProImage createProductImage(ProImage proImage) {
		return proImageRepository.save(proImage);
	}

	public void deleteProImage(Long proImgId) {
		proImageRepository.deleteById(proImgId);
	}

	public void deleteImageByProId(Long proId) {
		proImageRepository.deleteByProId(proId);
	}

	/**
	 * 根據產品ID查找所有相關圖片
	 */
	public List<ProImage> findAllImagesByProId(Long proId) {
		return proImageRepository.findAllByProId(proId);
	}

	public Optional<ProImage> findFirstImageByProId(Long proId) {
		return proImageRepository.findFirstByProId(proId);
	}
	
    public ProImage getProImageById(Long id) {
        Optional<ProImage> optionalImage = proImageRepository.findById(id);
        return optionalImage.orElse(null);
    }
    
//    public List<ProImage> findImagesByProId(Long proId) {
//        return proImageRepository.findByProId(proId);
//    }
}
