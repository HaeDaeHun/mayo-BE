package ukathon.mayo.domain.reference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ukathon.mayo.domain.reference.dto.ReferenceListResponse;
import ukathon.mayo.domain.reference.entity.Channel;

import java.util.List;

@Tag(name = "Reference", description = "레퍼런스 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/references")
public class ReferenceController {

    private final ReferenceService referenceService;

    @Operation(summary = "레퍼런스 모아보기", description = "채널별 마케팅 레퍼런스를 조회합니다.")
    @GetMapping
    public ReferenceListResponse<?> getReferencesByChannel(
            @RequestParam(name = "channel") Channel channel
    ) {
        return referenceService.getReferenceListResponse(channel);
    }
}
