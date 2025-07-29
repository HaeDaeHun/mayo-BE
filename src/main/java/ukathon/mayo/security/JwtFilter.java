package ukathon.mayo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ukathon.mayo.exception.CustomTokenException;
import ukathon.mayo.exception.ErrorCode;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

            // 부하 테스트용 토큰 예외 처리 (mocked_token)
            if ("mocked_token".equals(accessToken)) {
                Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                        new org.springframework.security.core.userdetails.User("mocked_user_id", "",
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(mockAuth);
                filterChain.doFilter(request, response);
                return;
            }

            try {
                jwtUtil.validateAccessToken(accessToken); // 엑세스 토큰 유효성을 검증
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증된 사용자 정보를 SecurityContext에 저장
            } catch (CustomTokenException e) {
                SecurityContextHolder.clearContext();
                if (e.getErrorCode().equals(ErrorCode.EXPIRED_ACCESS_TOKEN)) {
                    // 리프레시 토큰 검증을 위해 쿠키에서 리프레시 토큰 가져오기
                    String refreshToken = cookieUtil.getCookieValue(request, "refreshToken");
                    if (refreshToken != null) {
                        try {
                            // 리프레시 토큰의 유효성을 검증
                            jwtUtil.validateRefreshToken(refreshToken);
                            ErrorResponseUtil.writeErrorResponse(response, e.getErrorCode(), request.getRequestURI());
                            return;
                        } catch (CustomTokenException ex) {
                            ErrorResponseUtil.writeErrorResponse(response, ex.getErrorCode(), request.getRequestURI());
                            return;
                        }
                    } else {
                        ErrorResponseUtil.writeErrorResponse(response, ErrorCode.NO_COOKIE, request.getRequestURI());
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request,response);  // 다음 필터로 요청을 넘김
    }
}

