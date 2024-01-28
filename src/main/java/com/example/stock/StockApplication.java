package com.example.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockApplication {
    /*
    1. Lettuce
    - 구현이 간단하다.
    - spring data redis를 이용하면 lettuce가 기본이기에 별도의 라이브러리를 사용하지 않아도 된다.
    - spin lock 방식이기 때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis에 부하가 갈 수 있다.

    2. Redisson
    - 락 획득 재시도를 기본으로 제공한다.
    - pub-sub 방식으로 구현이 되어 있기 때문에 lettuce와 비교했을 때 redis에 부하가 덜 간다.
    - 별도의 라이브러리를 사용해야 한다.
    - lock을 라이브러리 차원에서 제공해주기 때문에 사용법을 공부해야 한다.

    ! 실무에서는?
    - 재시도가 필요하지 않은 lock은 lettuce 활용
    - 재시도가 필요한 경우에는 redisson 를 활

    1. Mysql
    - 이미 Mysql을 사용하고 있다면 별도의 비용없이 사용가능하다.
    - 어느 정도의 트래픽까지는 문제없이 활용이 가능하다.
    - Redis 보다는 성능이 좋지 않다.

    2. Redis
    - 활용중인 Redis가 없다면 별도의 구축비용과 인프라 관리비용이 발생한다.
    - Mysql 보다 성능이 좋다.

     */

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

}
