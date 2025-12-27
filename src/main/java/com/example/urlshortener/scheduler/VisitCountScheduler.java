package com.example.urlshortener.scheduler;

import com.example.urlshortener.annotation.DistributedLock;
import com.example.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitCountScheduler {

    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    @Scheduled(fixedRate = 60000)
    @DistributedLock(key = "VisitCountScheduler", waitTime = 0, leaseTime = 59)
    public void syncVisitCount(){
        // Redis에서 "visitCount:" 키로 시작하는 모든 키 가져오기
        Set<String> keys = redisTemplate.keys("visitCount:*");

        // 처리할 것이 없으면 종료
        if (keys == null || keys.isEmpty()){
            return;
        }

        for (String key : keys){
            // keys에서 shortId 추출
            String shortId = key.split(":")[1];
            // 해당 key값으로 방문 횟수 값 가져오기
            String countStr = redisTemplate.opsForValue().get(key);
            if (countStr != null){
                Long count = Long.parseLong(countStr);
                // DB에 반영
                urlRepository.increaseVisitCount(shortId, count);
                // redis에서 삭제
                redisTemplate.delete(key);
            }
        }

    }

}
