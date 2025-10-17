// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.farmtastic.act.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActCateRepository extends JpaRepository<ActCate, Integer> {
}