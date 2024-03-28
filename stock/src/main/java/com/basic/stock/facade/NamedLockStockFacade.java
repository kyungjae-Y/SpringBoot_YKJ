package com.basic.stock.facade;

import com.basic.stock.repository.LockRepository;
import com.basic.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {
    private final LockRepository lockRepository;
    private final StockService service;

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString()); // 락 획득
            service.decreaseStock(id, quantity); // 재고 감소 로직 수행
        } finally {
            lockRepository.releaseLock(id.toString()); // 락 반환
        }
    }
}
