package com.example.urlshortener.kafka;

import com.example.urlshortener.dto.VisitLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "visit-log-topic";

    /**
     * 방문 로그를 Kafka로 전송
     * @Param logDto 전송할 데이터
     */
    public void sendVisitLog(VisitLogDto logDto){
        // 전송 시작 로그
        log.info("Sending log to kafka {}", logDto);

        kafkaTemplate.send(TOPIC, logDto);
    }
}
