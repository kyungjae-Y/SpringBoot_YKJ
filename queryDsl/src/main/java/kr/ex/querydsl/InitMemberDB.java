package kr.ex.querydsl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import kr.ex.querydsl.entity.Member;
import kr.ex.querydsl.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMemberDB {
    private final InitMemberService initMemberService;

    @PostConstruct  // 생성자 실행되면 바로 호출
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @Autowired
        EntityManager em;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);
            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i, selectedTeam));
            }
            System.out.println("=============================");
        }
    }
}