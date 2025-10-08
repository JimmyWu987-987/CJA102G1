package com.farmtastic.style.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //不一定要加，但如果有實作方法就必須加
public interface StyRepository extends JpaRepository<Sty, Byte> {

}
