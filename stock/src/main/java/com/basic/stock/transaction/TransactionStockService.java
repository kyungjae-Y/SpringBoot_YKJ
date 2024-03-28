package com.basic.stock.transaction;

import com.basic.stock.service.StockService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionStockService {
    private final StockService stockService;

    public void decrease(Long id, Long quantity) {
        startTransaction(); // tx.begin()
        stockService.decreaseStock(id, quantity);
        endTransaction(); // tx.commit()
    }

    private void startTransaction() {
        System.out.println("트랙잭션 시작");
    }

    private void endTransaction() {
        System.out.println("트랜잭션 끝");
    }
}
