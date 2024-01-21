package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @BeforeEach
    void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
    }

    @Test
    void 재고감소() {
        stockService.decrease(1L, 1L);

        // 100 - 1 = 99
        Stock stock = stockRepository.findById(1L)
                .orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

//    @Test
//    void 동시에_100개의_요청() throws InterruptedException {
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//
//        // 다른 스레드에서 수행중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    stockService.decrease(1L, 1L);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        Stock stock = stockRepository.findById(1L)
//                .orElseThrow();
//        System.out.println("stock.getId() = " + stock.getId());
//        // 100 - (1 * 100) = 0 예상
//        // 레이스 컨디션이 발생해 실패한다.
//        assertEquals(0, stock.getQuantity());
//    }

    @Test
    void 재고감소_with_pessimistic() {
        pessimisticLockStockService.decrease(1L, 1L);

        // 100 - 1 = 99
        Stock stock = stockRepository.findById(1L)
                .orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

    /*
    select s1_0.id,s1_0.product_id,s1_0.quantity from stock s1_0 where s1_0.id=? for update
    뒤에 for update가 락을 걸고 데이터를 가져오는 부분이다.
    충돌이 빈번하게 일어난다면 optimistic lock보다 성능이 좋을 수 있다.
    lock을 통해 업데이트를 제어하기 때문에 데이터 정합성이 보장된다.

    단점으로는
    별도의 락을 잡기 때문에 성능 감소가 있을 수 있다.
     */
    @Test
    void 동시에_100개의_요청_with_pessimistic_lock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Stock stock = stockRepository.findById(1L)
                .orElseThrow();
        // 100 - (1 * 100) = 0 예상
        assertEquals(0, stock.getQuantity());
    }
}