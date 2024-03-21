package kr.study.jpa1.service;

import kr.study.jpa1.domain.Member;
import kr.study.jpa1.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 -> sql 저장소 빠진 것
public class MemberService {
    private final MemberJpaRepository memberRepository;

    @Transactional // 읽기, 쓰기(삭제, 수정)
    public Long join(Member member) throws IllegalStateException {
        validateMemberId(member);
        Member m = memberRepository.save(member);
        log.trace("saved member = {}", m);
        return m.getId();
    }

    public List<Member> getList() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    private void validateMemberId(Member member) throws IllegalStateException {
        if (memberRepository.findByLoginId(member.getLoginId()) != null) {
            throw new IllegalStateException("이미 존재하는 회원 아이디가 있습니다");
        }
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
