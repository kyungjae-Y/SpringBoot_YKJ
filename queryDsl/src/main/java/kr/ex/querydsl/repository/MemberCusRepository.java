package kr.ex.querydsl.repository;

import kr.ex.querydsl.dto.MemberSearchCond;
import kr.ex.querydsl.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberCusRepository {
    public List<MemberTeamDto> searchByBuilder(MemberSearchCond condition);

    public List<MemberTeamDto> search(MemberSearchCond condition);

    Page<MemberTeamDto> searchPageSimple(MemberSearchCond condition, Pageable pageable);

    Page<MemberTeamDto> searchPageComplex(MemberSearchCond condition, Pageable pageable);
}
