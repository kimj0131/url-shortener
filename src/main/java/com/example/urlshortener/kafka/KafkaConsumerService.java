package com.example.urlshortener.kafka;

import com.example.urlshortener.dto.VisitLogDto;
import com.example.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final UrlService urlService;

    @KafkaListener(topics = "visit-log-topic", groupId = "url-shortener-group")
    public void consume(VisitLogDto logDto){
        log.info("Received lof form Kafka: {}", logDto);

        urlService.saveVisitHistory(
                logDto.getUrlCacheDto(),
                logDto.getClientIp(),
                logDto.getUserAgent()
        );
    }

}
