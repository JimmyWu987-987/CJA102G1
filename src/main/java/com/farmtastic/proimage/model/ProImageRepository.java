package com.farmtastic.proimage.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProImageRepository extends JpaRepository<ProImage, Long> {

	// 查找第一張圖片 (用於列表頁預覽)
	Optional<ProImage> findFirstByProId(Long proId);

	ProImage findFirstByProIdOrderByProImgIdAsc(Long proId);

	// 查找所有圖片
	List<ProImage> findAllByProId(Long proId);

	// 依 ProId 刪除
	void deleteByProId(Long proId);

}
