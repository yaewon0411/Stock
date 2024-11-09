package com.my.stock.service;

import com.my.stock.domain.Stock;
import com.my.stock.repo.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity){
        //stock 조회
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);

        //재고를 감소시킨 뒤
        stock.decrease(quantity);

        //갱신된 값을 저장
        stockRepository.saveAndFlush(stock);
    }
}
