package kr.ex.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QueryDslApplication {
    // 스프링 빈 컨테이너에 우리가 직접 등록
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    public static void main(String[] args) {
        SpringApplication.run(QueryDslApplication.class, args);
    }
}
