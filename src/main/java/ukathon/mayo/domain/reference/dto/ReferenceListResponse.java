package ukathon.mayo.domain.reference.dto;

import java.util.List;

public record ReferenceListResponse<T>(
        List<T> references
) {
    public static <T> ReferenceListResponse<T> from(List<T> data) {
        return new ReferenceListResponse<>(data);
    }
}