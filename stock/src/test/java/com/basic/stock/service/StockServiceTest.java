package com.basic.stock.service;

import com.basic.stock.entity.Stock;
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
class StockServiceTest {
    @Autowired
    private StockService service;
    @Autowired
    private StockRepository repository;

    @BeforeEach
    public void insert() {
        Stock stock = new Stock(1L, 100L);
        repository.saveAndFlush(stock);
    }

    @Test
    public void decreaseTest() {
        service.decreaseStock(1L, 1L);
        Stock stock = repository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        System.out.println("count = " + stock.getQuantity());
        Assertions.assertThat(stock.getQuantity()).isEqualTo(99);
    }

    @Test
    public void orderSametime100Stock() throws InterruptedException {
        int threadCount = 100;
//        비동기를 편리하게 도와주는 클래스 - 동시에 32개 쓰레드 관리
        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        100개의 요청이 모두 끝날 때 까지 기다려야하므로 CountDownLatch 통해서
//        다른 쓰레드에서 수행되는 작업이 완료될 때 까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    service.decreaseStock(1L, 1L);
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 요청이 끝날 때 까지 대기 - 밑에 줄 실행 안함
        Stock stock = repository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        Assertions.assertThat(stock.getQuantity()).isEqualTo(0);
    }
}