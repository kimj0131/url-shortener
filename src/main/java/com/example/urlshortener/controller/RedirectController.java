package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlCacheDto;
import com.example.urlshortener.dto.VisitLogDto;
import com.example.urlshortener.kafka.KafkaProducerService;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;
    private final KafkaProducerService kafkaProducerService;

    // 리다이렉트
    // 예시: GET / abc12345 -> 구글로 이동
    @GetMapping("/{shortId}")
    public ResponseEntity<Void> redirect(@PathVariable String shortId, HttpServletRequest request){
        UrlCacheDto dto = urlService.getUrlCache(shortId);

        if (dto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        handleVisitLog(shortId, dto, request);

        // 302 Found 상태코드와 Location 헤더를 사용하여 리다이렉트 시킴
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(dto.getOriginalUrl()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private void handleVisitLog(String shortId, UrlCacheDto dto, HttpServletRequest request){
        String userAddr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        VisitLogDto visitLogDto = VisitLogDto.createDto(dto, userAddr, userAgent);
        kafkaProducerService.sendVisitLog(visitLogDto);

        urlService.increaseVisitCount(shortId);
    }

}
