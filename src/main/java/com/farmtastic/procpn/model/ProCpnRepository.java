package com.farmtastic.procpn.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmtastic.common.enums.IsActive;

public interface ProCpnRepository extends JpaRepository<ProCpnVO, Integer> {
	List<ProCpnVO> findByIsActive(IsActive isActive); // 查啟用/停用
}
