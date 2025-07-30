package ukathon.mayo.domain.reference.dto;

import ukathon.mayo.domain.reference.entity.Category;
import ukathon.mayo.domain.reference.entity.Reference;

public record ReferenceInstagramResponseDto(
        Category category,
        String contentUrl,
        String thumbnail
) {

    public static ReferenceInstagramResponseDto from(Reference reference) {
        return new ReferenceInstagramResponseDto(
                reference.getCategory(),
                reference.getContentUrl(),
                reference.getThumbnail()
        );
    }
}
