package com.basic.stock.facade;

import com.basic.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {
    private final OptimisticLockStockService service;

    public void decreaseStock(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                service.decreaseStock(id, quantity);
                return;
            } catch (Exception e) {
                System.out.println("접속 실패");
                Thread.sleep(50);
            }
        }
    }
}
