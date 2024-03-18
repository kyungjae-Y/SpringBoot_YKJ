package kr.boot.basic.config;

import kr.boot.basic.repository.JdbcMemberRepository;
import kr.boot.basic.repository.MemberRepository;
import kr.boot.basic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// 우리가 직접 @Repository, @Service -> 빈 생성
@RequiredArgsConstructor
@Configuration
public class SpringConfig {
    private final DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }

    @Bean
    public MemberService memberService(MemberRepository repository) {
        return new MemberService(repository);
    }
}
