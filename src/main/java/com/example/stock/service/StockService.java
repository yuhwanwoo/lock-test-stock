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

/*
    DB를 활용하여 정합성 문제를 해결하는 법
    1. Pessimistic Lock
    실제로 데이터에 락을 걸어 정합성을 맞추는 방법. exclusive lock을 걸게되면 다른 트랜잭션에서는
    lock이 해제되기전에 데이터를 가져갈 수 없게 됩니다.
    그러나 데드락이 걸릴 수 있기 때문에 주의해야 한다.

    2. Optimistic Lock
    실제로 Loock을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법.
    먼저 데이터를 읽을 후에 update 수행할 때 현재 내가 읽은 버전이 맞는지 확인하여 업데이트 한다.
    내가 읽은 버전에서 수정사항이 생겼을 경우에는 application에서 다시 읽은 후에 작업을 수행해야 한다.

    3. Named Lock
    이름을 가진 metadata lockin이다. 이름을 가진 lock을 획득한 후 해제할 때까지 다른 세션은 이 lock을
    획득할 수 없도록 한다. 주의할 점으로는 transaction이 종료될 때 Lock이 자동으로 해제되지 않는다. 그래서
    별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제된다.
    Pessimistic lock과의 차이점은 Pessimistic lock은 테이블이나 로우 단위로 걸지만
    named Lock은 메타데이터에 locking을 하는 방법
*/
}
