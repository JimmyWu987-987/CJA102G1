package com.farmtastic.news.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{

    List<News> findByNewsStatusOrderByNewsAtDesc(Integer newsStatus);
}
