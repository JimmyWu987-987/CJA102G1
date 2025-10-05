package com.farmtastic.member.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemRepository extends JpaRepository<Mem, Integer> {

		Mem findByMemAcc(String memAcc);
		
		Mem findByMemMobile(String memMobile);
		
		Mem findByMemEmail(String memEmail);
}
