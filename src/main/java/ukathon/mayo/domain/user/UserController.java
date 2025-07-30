package ukathon.mayo.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import ukathon.mayo.domain.user.dto.OnboardingRequestDto;

@Tag(name = "User", description = "유저 관련 API입니다.")
@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "소상공인 온보딩",
            description = "Role값을 Seller로 변경하고 SellerProfile을 생성합니다."
    )
    @PostMapping("/business")
    public ResponseEntity<Void> businessOnboard(
            @Valid @RequestBody OnboardingRequestDto dto
    ) {
        userService.sellerOnboard(dto);
        return ResponseEntity.ok().build();
    }
}
