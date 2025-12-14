package com.example.urlshortener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    // 인메모리
    // Key: 단축코드(shortId), Value: 원본URL(originalUrl)
    // private final Map<String, String> urlMap = new ConcurrentHashMap<>();

    private final UrlRepository urlRepository;


    // 2. 단축 URL 생성 로직
    public String shortenUrl(String originalUrl) {

        // 간단하게 UUID의 앞 8자리만 잘라서 사용 (중복 체크 로직은 생략)
        String shortId = UUID.randomUUID().toString().substring(0, 8);

        // http 프로토콜이 없으면 붙여줌 (리다이렉트 시 필요)
        if(!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")){
            originalUrl = "https://" + originalUrl;
        }

        UrlEntity urlEntity = new UrlEntity(shortId, originalUrl);
        urlRepository.save(urlEntity);
        return shortId;
    }

    // 3. 원본 URL 조회 로직
    @Transactional
    public String getOriginalUrl(String shortId) {
        UrlEntity urlEntity = urlRepository.findByShortId(shortId);

        // 방문 카운트 증가
        Long visitCount = urlEntity.getVisitCount();
        urlEntity.setVisitCount(++visitCount);

        if(urlEntity == null){
            return null;
        }
        return urlEntity.getOriginalUrl();
    }
}
