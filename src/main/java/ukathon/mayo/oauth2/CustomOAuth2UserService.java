package ukathon.mayo.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ukathon.mayo.domain.user.entity.Role;
import ukathon.mayo.domain.user.entity.User;
import ukathon.mayo.domain.user.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpServletRequest httpRequest;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        Map<String, Object> attrs = super.loadUser(userRequest).getAttributes();
        OAuth2Attributes oAuth2Attrs = OAuth2Attributes.of("kakao", attrs);

        // 1) 세션에서 role 가져오기
        String roleStr = (String) httpRequest.getSession().getAttribute("selectedRole");
        Role role = Role.DEFAULT;
        if (roleStr != null && !roleStr.isBlank()) {
            try {
                role = Role.valueOf(roleStr);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // 2) 이메일로 기존 사용자 찾기
        Optional<User> optionalUser = userRepository.findByEmail(oAuth2Attrs.getEmail());

        // 3) firstLogin 플래그 및 User 엔티티 결정
        boolean firstLogin;
        User user;
        if (optionalUser.isPresent()) {
            firstLogin = false;
            user = optionalUser.get();
        } else {
            firstLogin = true;
            String nickname = generateUniqueNickname();
            User newUser = oAuth2Attrs.toEntity(nickname, role);
            user = userRepository.save(newUser);
        }

        // 4) OAuth2UserDetails 생성 시 firstLogin 전달
        return new OAuth2UserDetails(
                attrs,
                "id",
                user.getEmail(),
                firstLogin,
                user.getRole().name(),
                user.getId()
        );
    }

    private String generateUniqueNickname() {
        final int MAX_LEN = 8;
        String[] prefixes = {
                "달콤한", "짭짤한", "매콤한", "새콤한", "구수한", "고소한", "얼큰한", "향긋한"
        };
        String suffixChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String nickname;
        do {
            String prefix = prefixes[(int)(Math.random() * prefixes.length)];
            StringBuilder suffix = new StringBuilder();
            int remain = MAX_LEN - prefix.length();
            for (int i = 0; i < remain; i++) {
                suffix.append(suffixChars.charAt((int)(Math.random() * suffixChars.length())));
            }
            nickname = (prefix + suffix).substring(0, MAX_LEN);
        } while (userRepository.existsByNickname(nickname));
        return nickname;
    }

}
