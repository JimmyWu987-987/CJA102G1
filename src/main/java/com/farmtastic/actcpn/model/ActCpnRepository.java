package com.farmtastic.actcpn.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActCpnRepository extends JpaRepository<ActCpnVO, Integer> {
	List<ActCpnVO> findByIsActive(Byte isActive); // 查卷啟用/停用
}
