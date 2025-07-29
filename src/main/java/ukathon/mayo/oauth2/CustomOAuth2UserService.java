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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpServletRequest httpRequest;   // 주입

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        Map<String, Object> attrs = super.loadUser(userRequest).getAttributes();
        OAuth2Attributes oAuth2Attrs = OAuth2Attributes.of("kakao", attrs);

        // 세션에서 role 꺼내기
        String roleStr = (String) httpRequest.getSession().getAttribute("selectedRole");
        Role role = Role.valueOf(roleStr);

        User user = userRepository.findByEmail(oAuth2Attrs.getEmail())
                .orElseGet(() -> {
                    oAuth2Attrs.setFirstLogin(true);
                    String nickname = generateUniqueNickname();
                    User newUser = oAuth2Attrs.toEntity(nickname, role);
                    return userRepository.save(newUser);
                });

        return new OAuth2UserDetails(
                attrs,
                "id",
                user.getEmail(),
                oAuth2Attrs.isNewUser(),
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