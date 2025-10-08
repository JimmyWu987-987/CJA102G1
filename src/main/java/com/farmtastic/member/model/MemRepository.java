package com.farmtastic.member.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemRepository extends JpaRepository<Mem, Integer> {

		Mem findByMemAcc(String memAcc);
		
		Mem findByMemMobile(String memMobile);
		
		Mem findByMemEmail(String memEmail);
}
