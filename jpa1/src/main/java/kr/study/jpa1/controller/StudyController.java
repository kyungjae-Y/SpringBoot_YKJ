package kr.study.jpa1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/study")
public class StudyController {
    @GetMapping
    public String addForm() {
        return "/study/addForm";
    }
    @PostMapping
    public String addStudyRecord() {
        return null;
    }
}
