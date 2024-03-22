package kr.ex.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.ex.querydsl.entity.Member;
import kr.ex.querydsl.entity.QMember;
import kr.ex.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ex.querydsl.entity.QMember.member;
import static kr.ex.querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class QueryDslTest2 {
    @Autowired
    EntityManager em;
    JPAQueryFactory query;

    @BeforeEach
    void initData() {
        // 쿼리 DSL 객체
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

    /**
     * JPQL
     * select
     * COUNT(m), //회원수
     * SUM(m.age), //나이 합
     * AVG(m.age), //평균 나이
     * MAX(m.age), //최대 나이
     * MIN(m.age) //최소 나이
     * from Member m
     */
    // Tuple -> queryDsl 에서 제공해주는 여러 자료형를 전부다 저장할 수 있는 객체
    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = query.select(member.count(), member.age.sum(), member.age.avg(), member.age.max(), member.age.min()).from(member).fetch();
        Tuple tuple = result.get(0); // count() 결과
        System.out.println("tuple.toString() = " + tuple.toString());
        System.out.println("count = " + tuple.get(member.count()));
        result.forEach(t -> System.out.println("t = " + t));
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     */
    @Test
    public void group() {
        List<Tuple> result = query.select(team.name, member.age.avg()).from(member).join(member.team, team)     // join 은 inner 조인을 의미한다  m.teamid = t.teamid
                .groupBy(team.name)
                //.having(member.age.avg().gt(10))
                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);
        System.out.println("teamA = " + teamA.toString());
        System.out.println("teamB = " + teamB.toString());
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 세타 조인(연관관계가 없는 필드로 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join() throws Exception {
        // 사람 이름이 teamA
        // 연관관겨는 id 만 있으니깐 name 으로도 되는지 test  : 연관관계 m.teamId = t.teamId
        em.persist(new Member("teamA")); // member.setName("teamA")
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));
        // select * from member m , team t where m.username = t.username;
        List<Member> result = query.select(member).from(member, team) // from 두개를 나열하는 것
                .where(member.username.eq(team.name)).fetch();
        result.stream().forEach(m -> System.out.println("m = " + m + "t =" + m.getTeam()));
        assertThat(result).extracting("username").containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and
     * t.name='teamA'
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = query.select(member, team).from(member).leftJoin(member.team, team).on(team.name.eq("teamA")).fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = query.selectFrom(member).where(member.age.eq(JPAExpressions.select(memberSub.age.max()).from(memberSub))).fetch();
        assertThat(result).extracting("age").containsExactly(40);
    }

    @Test
    public void basicCase() {
        List<String> result = query.select(member.age.when(10).then("열살").when(20).then("스무살").otherwise("기타")).from(member).fetch();
        result.stream().forEach(s -> System.out.println("s = " + s));
    }

    @Test
    public void complexCase() {
        List<String> result = query.select(new CaseBuilder().when(member.age.between(0, 20)).then("0~20살").when(member.age.between(21, 30)).then("21~30살").otherwise("기타")).from(member).fetch();
        result.stream().forEach(s -> System.out.println("s = " + s));
    }
}
