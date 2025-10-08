// com.farmtastic.shoppingcart.model.ProductRepository.java

package com.farmtastic.shoppingcart.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// 繼承 JpaRepository<Entity類別, 主鍵型別>
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Spring Data JPA 會自動實作 CRUD 方法
}