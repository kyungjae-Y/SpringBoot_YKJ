package kr.study.jpa1.form;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
@Builder // 생성할 때 만 초기값 넣을 수 있음
public class StudyForm {
    private String memberLoginId;
    private LocalDate studyDay; // 미래시간 선택 불가능, 현재 또는 과거 선택 가능
    private LocalTime startTime; // 오후 6 : 10
    private int studyMin; // 40
    private String contents;
}
