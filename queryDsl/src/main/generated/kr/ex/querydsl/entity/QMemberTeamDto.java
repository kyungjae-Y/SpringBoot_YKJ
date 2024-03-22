package kr.ex.querydsl.entity;

import com.querydsl.core.types.ConstructorExpression;
import kr.ex.querydsl.dto.MemberTeamDto;

import javax.annotation.processing.Generated;

/**
 * kr.ex.querydsl.domain.QMemberTeamDto is a Querydsl Projection type for MemberTeamDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberTeamDto extends ConstructorExpression<MemberTeamDto> {
    private static final long serialVersionUID = -1743379837L;

    public QMemberTeamDto(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<Integer> age, com.querydsl.core.types.Expression<Long> teamId, com.querydsl.core.types.Expression<String> teamName) {
        super(MemberTeamDto.class, new Class<?>[]{long.class, String.class, int.class, long.class, String.class}, memberId, username, age, teamId, teamName);
    }
}

