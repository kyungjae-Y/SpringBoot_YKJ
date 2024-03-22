package kr.ex.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.ex.querydsl.entity.Member;
import kr.ex.querydsl.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ex.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.*;
@Transactional
@SpringBootTest
class QueryDslTest1 {
    @Autowired
    EntityManager em;
    JPAQueryFactory query;

    @BeforeEach
    void initData(){
        // 쿼리DSL 객체
        query = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        // 영속성 컨텍스트 초기화
        em.flush();
        em.clear();
        System.out.println("==========================");
    }
    @Test
    void testDomain(){

        Member findMember = em.find(Member.class, 28L);
        assertThat(findMember.getUsername()).isEqualTo("member27");
    }

    @Test
    void searchByJPQL(){
        Member findMember = em.createQuery("select m from Member m where m.username=:username",Member.class)
                .setParameter("username","member1").getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchByQueryDsl(){
        Member findMember = query.selectFrom(member)
                .where(member.username.eq("member1").and(member.age.loe(30)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    void searchByQueryDsl2(){
        Member findMember = query.selectFrom(member)
                .where(member.username.eq("member1"),(member.age.loe(30)))  // 콤마로 들어오면 무조건 and 조건
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));
        List<Member> result = query
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();
        
        result.forEach(m-> System.out.println("m = " + m));
        
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    //조회 건수 제한
    @Test
    public void paging1() {
        List<Member> result = query
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작(zero index)
                .limit(2) //최대 2건 조회
                .fetch();  // 리스트만 반환
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("member3");
        assertThat(result.get(1).getUsername()).isEqualTo("member2");
    }

    //전체 조회 수가 필요하면?
    @Test
    public void paging2() {
        QueryResults<Member> queryResults = query
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();  // 쿼리문을 2개 날림 , count 쿼리랑 limit 날림

        assertThat(queryResults.getTotal()).isEqualTo(4); // 전체 count
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

}