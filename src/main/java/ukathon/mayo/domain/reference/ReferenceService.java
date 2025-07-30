package ukathon.mayo.domain.reference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ukathon.mayo.domain.reference.dto.ReferenceBlogResponseDto;
import ukathon.mayo.domain.reference.dto.ReferenceInstagramResponseDto;
import ukathon.mayo.domain.reference.dto.ReferenceListResponse;
import ukathon.mayo.domain.reference.entity.Channel;
import ukathon.mayo.domain.reference.entity.Reference;
import ukathon.mayo.exception.CustomException;
import ukathon.mayo.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    public ReferenceListResponse<?> getReferenceListResponse(Channel channel) {
        List<Reference> references = referenceRepository.findByChannel(channel);

        return switch (channel) {
            case N_BLOG -> ReferenceListResponse.from(
                    references.stream().map(ReferenceBlogResponseDto::from).toList()
            );
            case IG_REELS, IG_FEED -> ReferenceListResponse.from(
                    references.stream()
                            .map(ReferenceInstagramResponseDto::from)
                            .toList()
            );
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        };
    }
}

