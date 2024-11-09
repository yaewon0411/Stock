package com.my.stock.facade;

import com.my.stock.repo.RedisLockRepository;
import com.my.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)){
            Thread.sleep(100); //락 획득에 실패하면 100ms 후에 다시 재시도하도록
        }
        //락 획득에 성공했다면
        try{
            stockService.decrease(id, quantity);
        }finally {
            redisLockRepository.unLock(id);
        }
    }
}
