package kr.ex.querydsl.controller;

import kr.ex.querydsl.dto.MemberSearchCond;
import kr.ex.querydsl.dto.MemberTeamDto;
import kr.ex.querydsl.entity.Member;
import kr.ex.querydsl.repository.MemberJpaRepository;
import kr.ex.querydsl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    // private final MemberJpaRepository memberRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/home")
    public String home() {
        return "홈이야";
    }

    @GetMapping("/members")
    public List<Member> allMembers() {
        List<Member> memberlist = memberRepository.findAll();
        //List<Member> memberlist= memberRepository.findAll_QueryDsl();
        return memberlist;
    }

    @PostMapping("/member")
    public String addMember(@RequestBody Member member) {
        memberRepository.save(member);
        return "회원생성 완료";
    }

    @GetMapping("/member")   // /member?username=박연미
    public String findByUsername(@RequestParam(name = "username") String username) {
        log.trace("username={}", username);
        Member findMember = null;
        // if(memberRepository.findByUsername_QueryDsl(username).size()> 0 ){
        if (memberRepository.findByUsername(username).size() > 0) {
            findMember = memberRepository.findByUsername(username).get(0);
            // findMember =memberRepository.findByUsername_QueryDsl(username).get(0);
        }
        return findMember == null ? "해당 이름 회원없음" : findMember.toString();
    }
//    @GetMapping("/member/{username}")  // /member/1
//    public String findByUsername2(@PathVariable(name="username") String username){
//        log.trace("username={}" , username);
//        return "ok";
//    }


    @GetMapping("/member/{id}")  // /member/1
    public String findByUsername(@PathVariable(name = "id") Long id) {
        log.trace("id={}", id);
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.isEmpty() ? "해당 번호 회원없음" : findMember.toString();
    }


    @PostMapping("/member/search")
    public List<MemberTeamDto> searchMember(@RequestBody MemberSearchCond condition) {
        log.trace("condition = {}", condition);
        return memberRepository.searchByBuilder(condition);
    }

    @PostMapping("/member/search2")
    public List<MemberTeamDto> searchMember2(@RequestBody MemberSearchCond condition) {
        log.trace("condition = {}", condition);
        return memberRepository.search(condition);

    }

    @GetMapping("/member/page1")
    public Page<MemberTeamDto> searchMember3(MemberSearchCond condition, Pageable pageable) {
        System.out.println("========================== paging 1 ===================");
        return memberRepository.searchPageSimple(condition, pageable);
    }

    @GetMapping("/member/page2")
    public Page<MemberTeamDto> searchMember4(MemberSearchCond condition, Pageable pageable) {
        System.out.println("========================== paging 2 ===================");
        return memberRepository.searchPageComplex(condition, pageable);
    }
}
