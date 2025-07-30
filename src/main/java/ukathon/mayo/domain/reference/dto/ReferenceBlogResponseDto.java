package ukathon.mayo.domain.reference.dto;

import ukathon.mayo.domain.reference.entity.Category;
import ukathon.mayo.domain.reference.entity.Reference;

import java.time.LocalDate;

public record ReferenceBlogResponseDto(
        Category category,
        String title,
        String contents,
        String contentUrl,
        LocalDate date,
        String thumbnail
) {

    public static ReferenceBlogResponseDto from(Reference reference) {
        return new ReferenceBlogResponseDto(
                reference.getCategory(),
                reference.getTitle(),
                reference.getContents(),
                reference.getContentUrl(),
                reference.getDate(),
                reference.getThumbnail()
        );
    }
}
