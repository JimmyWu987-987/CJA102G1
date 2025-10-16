package com.farmtastic.admin.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Integer>{

	//查詢管理員
	Optional<Admin> findByAdminAcc(String adminacc);
	
}
