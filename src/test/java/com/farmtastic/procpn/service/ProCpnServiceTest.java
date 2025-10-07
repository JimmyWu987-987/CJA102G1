package com.farmtastic.procpn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.dto.ProCpnResponseDTO;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnServiceImp;
import com.farmtastic.procpn.model.ProCpnVO;

public class ProCpnServiceTest {
//	@Test
//	void testGetCoupon() {
//		// 1. 建立 Mock
//		ProCpnRepository repo = mock(ProCpnRepository.class);
//		ProCpnMapper mapper = mock(ProCpnMapper.class);
//
//		// 2. 準備假資料
//		ProCpnVO vo = new ProCpnVO();
//		vo.setProCpnId(1);
//		vo.setCpnName("生日折扣");
//
//		ProCpnResponseDTO dto = new ProCpnResponseDTO();
//		dto.setProCpnId(1);
//		dto.setCpnName("生日折扣");
//
	// 3. 設定 Mock 行為
//		when(repo.findById(1)).thenReturn(Optional.of(vo));
//		when(mapper.toResponseDTO(vo)).thenReturn(dto);
//
	// 4. 測試 Service
//		ProCpnServiceImp service = new ProCpnServiceImp(repo, mapper);
//		
//  
////		// 5. 驗證
////		assertEquals("生日折扣", result.getCpnName());
////		verify(repo).findById(1);
////		verify(mapper).toResponseDTO(vo);
//	}

	@Test
	void testFindAllProCpn() {
		// Arrange (準備假資料 & Mock)
		ProCpnRepository mockRepo = mock(ProCpnRepository.class);
		ProCpnMapper mockMapper = mock(ProCpnMapper.class);

		ProCpnVO vo = new ProCpnVO();
		vo.setProCpnId(1);
		vo.setCpnName("生日券");

		ProCpnResponseDTO dto = new ProCpnResponseDTO();
		dto.setProCpnId(1);
		dto.setCpnName("生日券");
		dto.setDiscountInfo("8折");
		dto.setExpDate(LocalDate.now().plusDays(7));
		dto.setStatus("ACTIVE");

		when(mockRepo.findAll()).thenReturn(List.of(vo));
		when(mockMapper.toResponseDTO(vo)).thenReturn(dto);

		ProCpnService service = new ProCpnServiceImp(mockRepo, mockMapper);

		// Act (執行要測的方法)
		List<ProCpnAdminDTO> result = service.findAllProCpn();

		// Assert (驗證結果)
		assertEquals(1, result.size());
		assertEquals("生日券", result.get(0).getCpnName());
		// assertEquals("8折", result.get(0).getDiscountInfo());

		// Verify (驗證 mock 是否真的被呼叫)
		verify(mockRepo, times(1)).findAll();
		verify(mockMapper, times(1)).toResponseDTO(vo);
	}
}
