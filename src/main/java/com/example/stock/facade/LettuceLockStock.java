package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStock {
    private final RedisLockRepository repository;
    private final StockService service;

    public LettuceLockStock(RedisLockRepository repository, StockService service) {
        this.repository = repository;
        this.service = service;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!repository.lock(key)) {
            Thread.sleep(100);
        }
        try {
            service.decrease(key, quantity);
        } finally {
            repository.unlock(key);
        }
    }
}
