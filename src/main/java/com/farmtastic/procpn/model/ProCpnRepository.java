package com.farmtastic.procpn.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProCpnRepository extends JpaRepository<ProCpnVO, Integer> {
	List<ProCpnVO> findByIsActive(Byte isActive); // 查啟用/停用
}
