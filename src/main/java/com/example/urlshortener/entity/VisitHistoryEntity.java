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
@Table(name = "visit_history")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private UrlEntity url;

    @Column(nullable = false)
    private String clientIp;

    private String userAgent;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime visitedAt;

    public VisitHistoryEntity(UrlEntity url, String clientIp, String userAgent) {
        this.url = url;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
