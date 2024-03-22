package kr.basic.security.config.auth;

import kr.basic.security.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소가 오면 낚아서 로그인 진행
// login 완료 -> security session 을 만든다 (Security ContextHolder)
// type --> authentication type object
// authentication 객체 --> user 정보를 넣어야함 => userDetails

public class PrincipalDetails implements UserDetails, OAuth2User {
    private Users user;
    private Map<String, Object> attributes;

    // 일단 로그인 객체
    public PrincipalDetails(Users user) {
        this.user = user;
    }

    public PrincipalDetails(Users user, Map<String, Object> attributes) {
        this.attributes = attributes; // 구글 로그인 할 때 프로필 정보 이메일이 넘겨옴
        this.user = user;
    }

    //  user 권한 넘겨준다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //  계정이 만료되지 않았는가 ?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //  계정이 잠가지지 않았는가 ?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //  user 비번이 기간이 지났는가 ?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //  계정이 활성화 되어있는가 ?
    @Override
    public boolean isEnabled() {
//      계정이 비활성화 될 때 : 휴면계정
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
