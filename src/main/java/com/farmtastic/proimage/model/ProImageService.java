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

	// 修改圖片
	@Transactional
	public ProImage updateProImage(Long proImgId, byte[] newImageBytes) {
		// 1. 根據 ID 查找圖片，如果找不到，findById 會回傳空 Optional
		ProImage existingImage = proImageRepository.findById(proImgId)
				.orElseThrow(() -> new NotFoundException("找不到要更新的圖片，ID: " + proImgId));

		// 2. 更新圖片的 byte[] 資料
		existingImage.setProImg(newImageBytes);

		// 3. 儲存變更 (因為在 @Transactional 環境下，這一步其實可以省略，JPA 會自動更新)
		return proImageRepository.save(existingImage);
	}

//    public List<ProImage> findImagesByProId(Long proId) {
//        return proImageRepository.findByProId(proId);
//    }
}
