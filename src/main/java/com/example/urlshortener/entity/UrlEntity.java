package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "urls", indexes ={ @Index(name = "idx_short_id", columnList = "shortId") })
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

    public UrlEntity(String shortId, String originalUrl) {
        this.shortId = shortId;
        this.originalUrl = originalUrl;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}
