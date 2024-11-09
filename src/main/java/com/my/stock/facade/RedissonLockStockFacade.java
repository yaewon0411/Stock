package com.my.stock.facade;

import com.my.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decrease(Long id, Long quantity){
        //락 객체 생성
        RLock lock = redissonClient.getLock(id.toString());

        try{
            //락 획득 시도
            //10초 동안 락을 획득을 시도하고, 획득하면 1초 동안 점유
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS); //최대 락 획득 대기 시간, 점유 시간, 점유 시간 단위

            if(!available){
                System.out.println("lock 획득 실패");
                return;
            }

            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            //락을 소유한 스레드만 해제 가능
            lock.unlock();
        }
    }
}
