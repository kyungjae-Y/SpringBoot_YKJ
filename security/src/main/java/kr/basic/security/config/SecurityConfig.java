package kr.basic.security.config;

import kr.basic.security.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity // 웹 필터에 시큐리티 필터를 적용해줌
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(new String[]{"/favicon.ico", "/resources**", "/error"});
        };
    }

    @Bean
    AuthenticationFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/user/**").authenticated() // 인증이 되면 누구나 들어올 수 있음
                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // role 이 매니저나 어드민만 들어올 수 있음
                .requestMatchers("/admin/**").hasAnyRole("ADMIN") // 어드민만 들어올 수 있음
                .anyRequest().permitAll()).formLogin(form -> {
            form.loginPage("/loginForm") // 우리가 만든 로그인 페이지로 자동 인터셉트
                    .loginProcessingUrl("/login").failureHandler(customAuthFailureHandler()).defaultSuccessUrl("/", true); // 로그인 성공하면 돌아올 페이지
        }).oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/loginForm")
//                    구글이 로그인 완료 후 처리 - 강제 회원가입
                    .userInfoEndpoint(userInfoEndpointConfig -> {
                        userInfoEndpointConfig.userService(principalOauth2UserService);
                    });
        });
        return http.build();
    }
}
