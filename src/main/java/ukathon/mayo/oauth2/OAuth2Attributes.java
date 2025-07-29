package ukathon.mayo.oauth2;
import lombok.Builder;
import lombok.Getter;
import ukathon.mayo.domain.user.entity.User;
import ukathon.mayo.domain.user.entity.Role;
import ukathon.mayo.exception.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class OAuth2Attributes {
    private String email;
    private boolean isNewUser;

    @Builder
    private OAuth2Attributes(String email, boolean isNewUser) {
        this.email = email;
        this.isNewUser = isNewUser;
    }

    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        if (!"kakao".equals(registrationId)) {
            throw new CustomException(ErrorCode.OAUTH2_LOGIN_FAILED);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        return OAuth2Attributes.builder()
                .email((String) account.get("email"))
                .isNewUser(false)
                .build();
    }

    /**
     * 변경된 User 빌더 시그니처에 맞춰 toEntity() 업데이트
     */
    public User toEntity(String nickname, Role role) {
        return User.builder()
                .email(this.email)
                .password("")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void setFirstLogin(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }
}