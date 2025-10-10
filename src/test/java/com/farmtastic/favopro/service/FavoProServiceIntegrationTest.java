package com.farmtastic.favopro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.farmtastic.favopro.model.FavoProId;
import com.farmtastic.favopro.model.FavoProRepository;
import com.farmtastic.favopro.model.FavoProServiceImp;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class FavoProServiceIntegrationTest {
	@Autowired
	private FavoProServiceImp favoProService;

	@Autowired
	private FavoProRepository favoProRepository;

	@DisplayName("新增收藏成功（整合測試）")
	@Test
	void testAddFavorite_Success() {
		Integer memId = 1;
		Integer proId = 2;

		// 執行新增收藏
		favoProService.addFavoPro(memId, proId);

		// 驗證是否真的存在於 DB
		boolean exists = favoProRepository.existsById(new FavoProId(memId, proId));
		assertThat(exists).isTrue();

		// ✅ 通過表示 Service → Repository → DB 流程都正確
	}

	@DisplayName("🚫 重複收藏應拋出 IllegalStateException")
	@Test
	void testAddFavorite_DuplicateShouldFail() {
		Integer memId = 1;
		Integer proId = 2;

		// 第一次新增成功
		favoProService.addFavoPro(memId, proId);

		// 第二次新增 → 應拋出例外
		assertThrows(IllegalStateException.class, () -> {
			favoProService.addFavoPro(memId, proId);
		});
	}
}
