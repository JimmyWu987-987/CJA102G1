package com.farmtastic.member.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemRepository extends JpaRepository<Mem, Integer> {

	Mem findByMemAcc(String memAcc);

	Mem findByMemMobile(String memMobile);

	Mem findByMemEmail(String memEmail);

	@Query("SELECT m FROM Mem m WHERE FUNCTION('MONTH', m.memBirthday) = :month AND FUNCTION('DAY', m.memBirthday) = :day")
	List<Mem> findByBirthday(@Param("month") int month, @Param("day") int day);
}
