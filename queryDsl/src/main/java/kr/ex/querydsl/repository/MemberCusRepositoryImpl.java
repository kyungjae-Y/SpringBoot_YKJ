package kr.ex.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.ex.querydsl.dto.MemberSearchCond;
import kr.ex.querydsl.dto.MemberTeamDto;
import kr.ex.querydsl.dto.QMemberTeamDto;
import kr.ex.querydsl.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.ex.querydsl.entity.QMember.member;
import static kr.ex.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MemberCusRepositoryImpl implements MemberCusRepository {
    private final JPAQueryFactory queryFactory; // queryDSL

    @Override
    public List<MemberTeamDto> searchByBuilder(MemberSearchCond condition) {
        BooleanBuilder builder = new BooleanBuilder();

        //StringUtils.hasText ==> null 과 빈문자열체크
        if (hasText(condition.getUserName())) {
            builder.and(member.username.eq(condition.getUserName()));
        }
        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }
        return queryFactory.select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name)).from(member).leftJoin(member.team, team).where(builder).fetch();
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCond condition) {
        return queryFactory.select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name)).from(member).leftJoin(member.team, team).where(usernameEq(condition.getUserName()), teamNameEq(condition.getTeamName()), ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe())).fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.isEmpty(username) ? null : member.username.eq(username);
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.isEmpty(teamName) ? null : team.name.eq(teamName);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }

    // 페이징 처리
    // 단순한 페이징, fetchResults() 사용
    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCond condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory.select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name)).from(member).leftJoin(member.team, team).where(usernameEq(condition.getUserName()), teamNameEq(condition.getTeamName()), ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()))     //?page=숫자값&size=숫자값
                .offset(pageable.getOffset())  // 몇번째부터 조회할꺼야
                .limit(pageable.getPageSize()) // 몇개까지 조회할꺼야
                .fetchResults();
        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    // 복잡한 페이징 => 데이터 조회 쿼리와, 전체 카운트 쿼리를 분리
    // 데이터가 많지 않으면 굳이 분리 해서 안해도 됨 데이터가 천개가 넘으면 고민하기
    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCond condition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory.select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name)).from(member).leftJoin(member.team, team).where(usernameEq(condition.getUserName()), teamNameEq(condition.getTeamName()), ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe())).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        // 내가 직접 토탈카운트를 날림
        // join 을 하지 않아도 토탈 카운트에 영향을 미치치않으면
        // fetchResult 가 아니라 각각 날려서 join 을 최소화해서
        // 성능 최적화 할 수 있다
        JPAQuery<Member> countQuery = queryFactory.select(member).from(member).leftJoin(member.team, team).where(usernameEq(condition.getUserName()), teamNameEq(condition.getTeamName()), ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()));
        countQuery.fetch();
        // return new PageImpl<>(content, pageable, total);
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }
}
