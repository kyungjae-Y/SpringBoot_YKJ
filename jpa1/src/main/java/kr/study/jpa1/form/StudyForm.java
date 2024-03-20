package kr.study.jpa1.form;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder // 생성할 때 만 초기값 넣을 수 있음
public class StudyForm {
    private String memberId;
    private String studyDay; // 미래시간 선택 불가능, 현재 또는 과거 선택 가능
    private String startTime; // 오후 6 : 10
    private int studyMins; // 40
    private String contents;
}
