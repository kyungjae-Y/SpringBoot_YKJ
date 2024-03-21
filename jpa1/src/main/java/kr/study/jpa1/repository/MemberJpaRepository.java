package kr.study.jpa1.repository;

import jakarta.persistence.EntityManager;
import kr.study.jpa1.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {
    private final EntityManager em;

//    @RequiredArgsConstructor 이거 대신 밑에로도 가능
//    @Autowired
//    public MemberJpaRepository(EntityManager em) {
//        this.em = em;
//    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public Member findById(Long id) {
        return em.createQuery("select m from Member m where m.id =: id", Member.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public Member findByLoginId(String loginId) {
        List<Member> members = em.createQuery("select m from Member m where m.loginId = : loginId", Member.class).setParameter("loginId", loginId).getResultList();
//        return members == null ? null : members.get(0);
        return members.stream().findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
//        삭제한 row 개수 = int
        int delCnt = em.createQuery("delete from Member m where m.id = : id").setParameter("id", id).executeUpdate();
        if (delCnt == 0) {
            log.error("errMSG = {}", "삭제 실패");
        }
    }
}
