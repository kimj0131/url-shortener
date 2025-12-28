package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "urls", indexes ={ @Index(name = "idx_short_id", columnList = "shortId") })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;

    @Column(nullable = false, unique = true)
    private String shortId;

    @Column(nullable = false, length = 1000)
    private String originalUrl;

    @Column(nullable = false)
    private Long visitCount = 0L;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public UrlEntity(String shortId, String originalUrl) {
        this.shortId = shortId;
        this.originalUrl = originalUrl;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}
