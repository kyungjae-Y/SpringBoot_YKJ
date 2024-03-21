package kr.study.jpa1.repository;

import kr.study.jpa1.domain.Member;
import kr.study.jpa1.domain.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    @Query(value = "select m.member_id, r.study_id, r.contents, r.start_time, r.study_day, r.study_min from member m, study_record r where m.member_id = r.member_id", nativeQuery = true)
    List<StudyRecord> selectAll();

    @Query(value = "select * from study_record where member_id = :memberId", nativeQuery = true)
    List<StudyRecord> searchStudyRecordByMemberId(@Param(value = "memberId") Long memberId);

//    delete study_record where member_id = memberId;
    void deleteByMember(Member member);
}
