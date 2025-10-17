package com.farmtastic.admin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTypeRepository extends JpaRepository<AdminType, Integer> {
    // JpaRepository 已經提供了所有基本的 CRUD (新增、讀取、更新、刪除) 功能
}
