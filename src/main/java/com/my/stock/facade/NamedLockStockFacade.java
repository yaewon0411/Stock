package com.my.stock.facade;

import com.my.stock.repo.LockRepository;
import com.my.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {
    private final LockRepository lockRepository;
    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity){
        try{
            //즉시 락 획득 & 다른 세션에서 확인 가능 -> 커밋 여부와 관계없이 바로 DB 레벨에서 설정된다
            lockRepository.getLock(id.toString()); //Lock 관리 트랜잭션
            stockService.decrease(id, quantity); //비즈니스 트랜잭션 (서비스의 decrease()를 REQUIRES_NEW 트랜잭션에서 실행한다면)
        }finally{
            lockRepository.releaseLock(id.toString()); //Lock 관리 트랜잭션
        }
    }
}
