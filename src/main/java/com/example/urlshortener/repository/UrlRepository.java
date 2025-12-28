package com.example.urlshortener.repository;

import com.example.urlshortener.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByShortId(String shortId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UrlEntity u SET u.visitCount = u.visitCount + :count WHERE u.shortId = :shortId")
    void increaseVisitCount(@Param("shortId") String shortId, @Param("count") Long count);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
}
