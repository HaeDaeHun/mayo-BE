package ukathon.mayo.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ukathon.mayo.security.CookieUtil;
import ukathon.mayo.security.JwtUtil;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private static final String OAUTH_PATH = "/HomeLogin";
    private static final String ONBOARDING_PATH = "/onboarding/step1";
    private static final String DEPLOYED_REDIRECT_URL = "https://mayo-fe.vercel.app";
    private static final List<String> ALLOWED_REDIRECT_URLS = List.of(
            "http://localhost:3000",
            "http://localhost:8080"
    );

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            // 1) 허용된 출처(origin 또는 referer) 판단
            String origin  = request.getHeader("origin");
            String referer = request.getHeader("referer");
            String baseRedirectUrl = ALLOWED_REDIRECT_URLS.stream()
                    .filter(u -> u.equals(origin) || (referer != null && referer.startsWith(u)))
                    .findFirst()
                    .orElse(DEPLOYED_REDIRECT_URL);

            // 2) JWT 발급 및 refreshToken 쿠키 저장
            String accessToken  = jwtUtil.generateAccessToken(authentication);
            int    expiresIn    = jwtUtil.getAccessTokenMaxAge();
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            cookieUtil.addCookie(response, "refreshToken",
                    refreshToken, jwtUtil.getRefreshTokenMaxAge());

            // 3) 첫 로그인 여부, userId 추출
            OAuth2UserDetails userDetails = (OAuth2UserDetails) authentication.getPrincipal();
            boolean isNewUser = userDetails.isNewUser();
            Long    userId    = userDetails.userId();
            log.info("OAuth2 로그인 {} 성공, accessToken={}",
                    isNewUser ? "첫 가입" : "기존 로그인", accessToken);

            // 4) 분기: 신규 유저면 온보딩, 아니면 기본 OAuth 경로
            String targetPath = isNewUser ? ONBOARDING_PATH : OAUTH_PATH;
            String redirectUrl = baseRedirectUrl + targetPath;

            // 5) 최종 리다이렉트 URL에 파라미터 붙이기
            String redirectUrlWithParams = UriComponentsBuilder
                    .fromUriString(redirectUrl)
                    .queryParam("accessToken", accessToken)
                    .queryParam("expiresIn", expiresIn)
                    .queryParam("isNewUser", isNewUser)
                    .queryParam("userId", userId)
                    .build()
                    .toUriString();

            response.sendRedirect(redirectUrlWithParams);
        } catch (Exception e) {
            log.error("소셜 로그인 처리 중 오류: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "소셜 로그인 실패");
        }
    }
}
