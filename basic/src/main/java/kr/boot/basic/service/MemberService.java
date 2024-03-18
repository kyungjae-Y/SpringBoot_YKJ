package kr.boot.basic.service;

import jakarta.transaction.Transactional;
import kr.boot.basic.domain.Member;
import kr.boot.basic.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository repository) {
        this.memberRepository = repository;
    }

    //    회원가입
    public boolean join(Member member) {
        if (validateDuplicateMember(member)) {
            memberRepository.save(member);
            return true;
        } else {
            System.out.println(" 이미 존재하는 회원 입니다 ");
            return false;
        }
    }

    // 이름 중복검사
    private boolean validateDuplicateMember(Member member) {
//       if(memberRepository.findById(member.getId()) != null){
//           throw new IllegalArgumentException("이미 존재하는 회원입니다");
//       }
//        memberRepository.findById(member.getId())
//                .ifPresent( m -> {throw new IllegalArgumentException("이미 존재하는 회원입니다");});
        return memberRepository.findByName(member.getName()).isEmpty();
    }

    // 전체 회원조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회
    public Optional<Member> findOneMember(Long id) {
        return memberRepository.findById(id);
    }


}
