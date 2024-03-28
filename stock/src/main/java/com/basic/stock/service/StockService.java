package com.basic.stock.service;

import com.basic.stock.entity.Stock;
import com.basic.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {
    private final StockRepository repository;
//    propagation = Propagation.REQUIRES_NEW - 에러는 전파가 안되고 자식만 따로 트랜잭션 열어서 실행하겠다
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Long id, Long quantity) {
//        stock 조회
        Stock stock = repository.findById(id).orElseThrow();
//        재고 감소
        stock.decreaseStock(quantity);
//        바로 갱신된 값을 db 저장
        repository.saveAndFlush(stock);
    }
}
