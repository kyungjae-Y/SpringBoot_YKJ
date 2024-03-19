package kr.boot.basic.repository;

import kr.boot.basic.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository  --> JpaRepository 상속받으면 자동으로 붙여짐
public interface SpringMemberRepository extends JpaRepository<Member, Long> {
    public Member findByName(String name);  // select *
}