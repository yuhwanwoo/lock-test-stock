package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
Pessimistic Lock은 Stock에 락을 걸지만
Named Lock은 별도의 공간에 락을 건다.
세션1이 1이라는 이름으로 락을 건다면 다른 세션은 세션1이 락을 해제한 후에 획득할 수 있게 된다.

실제로 사용할 때는 datasource를 분리하는 것을 추천한다. => connection pool이 부족해서 다른서비스에도 영향을 줄 수 있기 때문
 */
public interface LockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);
}
