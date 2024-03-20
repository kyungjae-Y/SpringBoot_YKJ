package kr.study.jpa1.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class StudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyId;
    private LocalDate studyDay; // 미래시간 선택 불가능, 현재 또는 과거 선택 가능
    private LocalTime startTime; // 오후 6 : 10
    private int studyMins; // 40
    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
}
