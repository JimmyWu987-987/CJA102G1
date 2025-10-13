package com.farmtastic.favopro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.farmtastic.favopro.model.FavoProId;
import com.farmtastic.favopro.model.FavoProRepository;
import com.farmtastic.favopro.model.FavoProServiceImp;

// Mockito模擬測試
@ExtendWith(MockitoExtension.class)
public class FavoProServiceMockTest {
	@Mock
	private FavoProRepository favoProRepository;
	@InjectMocks
	private FavoProServiceImp favoProService;

	@Test
	@DisplayName("測試收藏存在的情況")
	void testIsFavorite_True() {
		// 模擬資料存在
		when(favoProRepository.existsById(any(FavoProId.class))).thenReturn(true);

		boolean result = favoProService.isFavorite(1, 1);

		assertThat(result).isTrue();
		verify(favoProRepository).existsById(any(FavoProId.class));
	}

	@Test
	@DisplayName("測試收藏不存在的情況")
	void testIsFavorite_False() {
		when(favoProRepository.existsById(any(FavoProId.class))).thenReturn(false);

		boolean result = favoProService.isFavorite(1, 1001);

		assertThat(result).isFalse();
	}
}
