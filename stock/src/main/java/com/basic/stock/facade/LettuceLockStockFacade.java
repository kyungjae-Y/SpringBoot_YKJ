package com.basic.stock.facade;

import com.basic.stock.repository.RedisLockRepository;
import com.basic.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {
    private final RedisLockRepository repository;
    private final StockService service;

    // 실패 할 때 재시도 할 수 있는 로직
    public void decreaseStock(Long id, Long quantity) throws InterruptedException {
//        repository.lock(id) == true : lock 획득
//        repository.lock(id) == false : lock 획득 실패
        while (!repository.lock(id)) {
            Thread.sleep(100);
            System.out.println("test");
        }
//        획득 성공 후 -> 재고 감소 로직 실행
        try {
            service.decreaseStock(id, quantity);
        } catch (Exception e) {
            System.out.println("재고 감소 실패");
        } finally {
            repository.unlock(id);
        }
    }
}
