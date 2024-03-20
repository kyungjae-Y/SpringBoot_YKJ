package kr.study.jpa1.form;

import lombok.Data;

@Data
// 화면에서 받아온 member data
public class MemberForm {
    private String id;
    private String pw;
    private String name;
}
