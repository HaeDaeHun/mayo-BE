package ukathon.mayo.domain.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 및 토큰 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "리프레시 토큰 재발급",
            description = "리프레시 토큰과 액세스 토큰을 받아 새로운 토큰을 재발급합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken,
                                          @RequestParam final String accessToken,
                                          HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshTokens(accessToken, refreshToken, response));
    }
}