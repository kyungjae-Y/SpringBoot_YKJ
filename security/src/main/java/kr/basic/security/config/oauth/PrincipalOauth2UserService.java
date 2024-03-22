package kr.basic.security.config.oauth;

import kr.basic.security.config.auth.PrincipalDetails;
import kr.basic.security.config.oauth.provider.GoogleUserInfo;
import kr.basic.security.config.oauth.provider.NaverUserInfo;
import kr.basic.security.config.oauth.provider.OAuth2UserInfo;
import kr.basic.security.entity.RoleUser;
import kr.basic.security.entity.Users;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //    userRequest 는 구글에서 코드를 받아서 accessToken 을 응답 받는 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("user request clientRegistration : " + userRequest.getClientRegistration());
        System.out.println("user request getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest); // google 회원 프로필 조회
        System.out.println("get Attribute : " + oAuth2User.getAttributes());
//        회원 가입 강제로 진행
        String provider = userRequest.getClientRegistration().getClientId();
        System.out.println("provider = " + provider);
        String providerId = oAuth2User.getAttribute("sub");
        System.out.println("providerId = " + providerId);
        String username = provider + "_" + providerId; // google_123123123
        String password = bCryptPasswordEncoder.encode("test"); // 사실 비번이 필요없음
        String email = oAuth2User.getAttribute("email");
//        이미 기존에 구글로 로그인을 한 회원인지 아닌지 체크
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            user = Users.builder().username(username).password(password).email(email).role(RoleUser.ROLE_USER).provider(provider).providerId(providerId).build();
            userRepository.save(user);
        }
//        loadUser -> Authentication 객체 안에 들어간다
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2User processOAuthUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // Attribute 를 파싱해서 공통 객체로 묶는다. 관리가 편함
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("요청 실패");
        }
        // System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
        // System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
        Optional<Users> userOptional =
                userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
        Users user;
        if (!userOptional.isPresent()) {
            // user 의 패스워드가 null 이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음
            user = Users.builder()
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .role(RoleUser.ROLE_USER)
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
