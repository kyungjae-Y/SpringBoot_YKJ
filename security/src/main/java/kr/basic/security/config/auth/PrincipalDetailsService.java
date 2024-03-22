package kr.basic.security.config.auth;

import kr.basic.security.entity.Users;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// /login 자동 UserDetailsService 타입으로 IoC loadUserByUserName();
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //   Security session = Authentication = userDetails
    //   session (내부 Authentication (내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            System.out.println("유저 디테일 객체 생성" + userEntity);
            return new PrincipalDetails(userEntity); // 이 함수가 종료가 될 때 @Authentication 객체가 만들어진다
        }
        return null;
    }
}
