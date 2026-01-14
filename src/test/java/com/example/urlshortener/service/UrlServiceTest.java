package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    @DisplayName("새로운 URL을 단축하면 Short ID가 생성")
    void shortenUrl_New(){
        // 테스트 준비
        String originalUrl = "https://google.com";

        // 1. DB에 해당 URL이 없다고 가정
        given(urlRepository.findByOriginalUrl(originalUrl)).willReturn(Optional.empty());

        // 2. save() 호출 시, ID가 100인 Entity가 저장되었다고 가정
        given(urlRepository.save(any(UrlEntity.class))).willAnswer(invocation -> {
            UrlEntity entity = invocation.getArgument(0);
            // Reflection으로 id 강제 주입
            ReflectionTestUtils.setField(entity, "urlId", 100L);
            return entity;
        });

        // 실행
        String shortId = urlService.shortenUrl(originalUrl);
        // 검증 (id가 100일때 base62 값은?)
        assertThat(shortId).isEqualTo("C1");

        // save가 1번 호출되었는지 확인
        verify(urlRepository).save(any(UrlEntity.class));
    }

    @Test
    @DisplayName("Short ID로 URL 조회 성공")
    void findByShortId_Success(){
        // 테스트 준비
        String shortId = "C1";
        String originalUrl = "https://google.com";
        UrlEntity entity = new UrlEntity(shortId, originalUrl);

        given(urlRepository.findByShortId(shortId)).willReturn(Optional.of(entity));
        // 실행
        UrlEntity result = urlService.findUrlByShortId(shortId);
        // 검증
        assertThat(result.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("존재하지 않는 Short ID 조회 시 예외 발생")
    void findUrlByShortId_Fail() {
        // 테스트 준비
        String shortId = "XX";
        given(urlRepository.findByShortId(shortId)).willReturn(Optional.empty());

        // 실행 및 검증
        assertThrows(
                UrlNotFoundException.class,
                () -> urlService.findUrlByShortId(shortId)
        );
    }
}
