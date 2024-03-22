package kr.basic.security.controller;

import kr.basic.security.config.auth.PrincipalDetails;
import kr.basic.security.entity.RoleUser;
import kr.basic.security.entity.Users;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String home() {
        return "index";
    }

    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Users user) {
        log.trace("user = {}", user);
        int num = new Random().nextInt(0, 2);
        if (num == 0) {
            user.setRole(RoleUser.ROLE_USER);
        } else if (num == 1) {
            user.setRole(RoleUser.ROLE_MANAGER);
        } else {
            user.setRole(RoleUser.ROLE_ADMIN);
        }
        String initPassword = user.getPassword();
        String enPassword = bCryptPasswordEncoder.encode(initPassword);
        user.setPassword(enPassword);
        Users addUser = userRepository.save(user);
        log.trace("addUser = {}", addUser);
        return "redirect:/loginForm";
    }

    @GetMapping("/test")
    @ResponseBody
    public PrincipalDetails test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return null;
        }
        return principalDetails;
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/auth/login")
    public @ResponseBody String login(String error, String exception) {
        log.error("error = {}, exception = {}", error, exception);
        return exception.toString();
    }

    @GetMapping("/test/oauth")
    public @ResponseBody OAuth2User testLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        if(authentication == null) {
            return null;
        }
        OAuth2User oAuth2User2 = (OAuth2User) authentication.getPrincipal();
        log.error("oauth = {}", oauth);
        return oAuth2User2;
    }
}
