package kr.ex.querydsl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
// ctrl + shift + t => 테스트 파일 생성 자동으로 해줌
public class Hello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
