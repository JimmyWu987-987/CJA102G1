package com.farmtastic.procate.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcateRepository extends JpaRepository<Procate, Integer> {
}
