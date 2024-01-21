package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

/*
    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
    자바에서 synchronized를 사용하면 한 개의 스레드만 접근하도록 할 수 있다.

    그러나 이렇게만하면 실패하게 된다 그 이유는 @Transactional 때문인데
    스프링은 @Transactional 붙은 클래스를 래핑한 클래스를 새로 만든다.

    대충
    public class TransactionStockService {

        private StockService stockService;

        public void decrease(Long ... {
         startTransaction();

         stockService.decrease(id, quantity);

         endTransaction();

        }
    }

    그러면 db가 업데이트 하기 전에 다른 스레드가 호출할 수 있게 됨 -> 갱신전 값을 가져가므로 동일한 문제 발생
    @Transactional 제거하면 일단 해결할 수 있다.
    그러나 이것도 완벽한 해결책이 아니다. synchronized는 하나의 프로세스에서만 보장이 된다.
    서버 2대 이상일 때는 보장해줄 수 없다.

 */


//    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
