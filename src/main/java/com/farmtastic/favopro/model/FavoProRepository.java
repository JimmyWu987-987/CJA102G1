package com.farmtastic.favopro.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoProRepository extends JpaRepository<FavoProVO, FavoProId> {

}
