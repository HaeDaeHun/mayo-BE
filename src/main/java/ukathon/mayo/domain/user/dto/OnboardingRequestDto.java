package ukathon.mayo.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import ukathon.mayo.domain.matching.Region;
import ukathon.mayo.domain.reference.Channel;
import ukathon.mayo.domain.reference.Category;
import ukathon.mayo.domain.user.entity.Role;

public record OnboardingRequestDto(
        @NotNull Role role,
        @NotNull Category category,
        @NotNull Region storeRegion,
        @NotNull Channel preferredChannel
) {}
