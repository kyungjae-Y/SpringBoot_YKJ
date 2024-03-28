package com.basic.stock.service;

import com.basic.stock.entity.Stock;
import com.basic.stock.facade.OptimisticLockStockFacade;
import com.basic.stock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OptimisticLockStockServiceTest {
    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;  // 재시도 해주는 로직 객체
    @Autowired
    private StockRepository repository;

    @BeforeEach
    public void insert() {
        Stock stock = new Stock(1L, 100L);
        repository.saveAndFlush(stock);
    }

    @Test
    public void orderSametime100Stock() throws InterruptedException {
        int treadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(treadCount);
        for (int i = 0; i < treadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decreaseStock(1L, 1L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 요청이 끝날때까지 대기 -- 밑에줄 실행 안함
        Stock stock = repository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        Assertions.assertThat(stock.getQuantity()).isEqualTo(0);
    }
}