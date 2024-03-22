package kr.ex.querydsl.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "members")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String name;
    @OneToMany(mappedBy = "team")
    @JsonManagedReference
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}