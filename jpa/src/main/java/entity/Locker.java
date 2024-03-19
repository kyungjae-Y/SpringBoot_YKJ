package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int lockNo;
//  관계형 데이터베이스 mysql 에서는 생성이 안된다
    @OneToOne(mappedBy = "locker")
    private Student student;

    public Locker(int lockNo) {
        this.lockNo = lockNo;
    }
}
