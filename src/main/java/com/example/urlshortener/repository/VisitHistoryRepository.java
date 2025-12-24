package com.example.urlshortener.repository;

import com.example.urlshortener.entity.VisitHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitHistoryRepository extends JpaRepository<VisitHistoryEntity, Long> {

}

