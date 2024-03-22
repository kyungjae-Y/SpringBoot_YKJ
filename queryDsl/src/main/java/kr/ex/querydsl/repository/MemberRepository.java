package kr.ex.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import kr.ex.querydsl.dto.MemberSearchCond;
import kr.ex.querydsl.dto.MemberTeamDto;
import kr.ex.querydsl.dto.QMemberTeamDto;
import kr.ex.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static kr.ex.querydsl.entity.QMember.member;
import static kr.ex.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCusRepository {
    List<Member> findByUsername(String name);
}
