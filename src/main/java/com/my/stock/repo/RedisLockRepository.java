package com.my.stock.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    //분산 락 획득 메서드 -> 락 획득 성공 시 true, 실패 시 false 반환
    public Boolean lock(Long key){
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000)); //setIfAbsent == setnx.  락 만료 시간 3초로 설정
    }
    //락 해제 메서드 -> 락 해제 성공 시 true, 실패 시 false 반환
    public Boolean unLock(Long key){
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
