package com.example.urlshortener.controller;

import com.example.urlshortener.common.ApiResponse;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.service.UrlService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UrlController {

    private final UrlService urlService;

    // API 1: 단축 URL 생성
    // 예시: POST /api/urls?url=https://www.google.com
    @PostMapping("/urls")
    public ResponseEntity<ApiResponse<String>> shorten(@RequestParam @NotBlank @URL String url){

        String shortId = urlService.shortenUrl(url);

        // 실제 서비스라면 도메인을 붙여서 리턴 (예: http://localhost:8080/abc12345)
        return ResponseEntity.ok(ApiResponse.success(shortId));
    }

    // API 2: 정보 조회
    @GetMapping("/urls/{shortId}")
    public ResponseEntity<ApiResponse<UrlResponseDto>> getUrlInfo(@PathVariable String shortId) {

        UrlResponseDto urlResponseDto = UrlResponseDto.from(urlService.findUrlByShortId(shortId));

        return ResponseEntity.ok(ApiResponse.success(urlResponseDto));
    }

}
