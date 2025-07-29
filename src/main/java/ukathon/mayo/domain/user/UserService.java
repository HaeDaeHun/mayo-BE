package ukathon.mayo.domain.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ukathon.mayo.domain.user.dto.OnboardingRequestDto;
import ukathon.mayo.domain.user.entity.SellerProfile;
import ukathon.mayo.domain.user.entity.User;
import ukathon.mayo.domain.user.repository.SellerProfileRepository;
import ukathon.mayo.domain.user.repository.UserRepository;
import ukathon.mayo.exception.CustomException;

import ukathon.mayo.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;

    @Transactional
    public void sellerOnboard(OnboardingRequestDto dto) {
        User user = getCurrentUser();

        boolean exists = sellerProfileRepository.existsByUserId(user.getId());
        if (exists) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS);
        }

        user.changeRole(dto.role());
        SellerProfile profile = SellerProfile.builder()
                .user(user)
                .storeName(user.getNickname() + " 매장")
                .category(dto.category())
                .storeRegion(dto.storeRegion())
                .address("") // TODO: 마이페이지
                .preferredChannel(dto.preferredChannel())
                .isVerified(false)
                .build();
        sellerProfileRepository.save(profile);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User getCurrentUser() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        return user;
    }
}
