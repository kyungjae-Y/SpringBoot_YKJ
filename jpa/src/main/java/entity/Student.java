package entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student_tbl")
@ToString(exclude = "major") // 연관관계가 있는 필드는 toString 을 제외를 해줘야 한다
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    private String name;
    private String grade;
    @ManyToOne(fetch = FetchType.LAZY)  // 관계 구성
//    fetch = FetchType.EAGER 즉시 로딩 - 연관되어있는 모든 (기본값)
//    fetch = FetchType.LAZY 지연 로딩
    @JoinColumn(name = "majorId")// 테이블 컬럼의 fk 명
    private Major major;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Locker locker;

    public Student(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }
}
