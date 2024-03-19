package entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "major_tbl")
@ToString(exclude = "students")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long majorId;
    private String name;
    private String category;
//  관계형 데이터베이스 mysql 에서는 생성이 안된다
    @OneToMany(mappedBy = "major") // 연관관계 주인인 student table 의 major
    private List<Student> students = new ArrayList<>(); // 읽기 전용

    public Major(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
