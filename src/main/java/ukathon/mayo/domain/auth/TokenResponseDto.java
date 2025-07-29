package ukathon.mayo.domain.auth;

public record TokenResponseDto(
        String accessToken,
        long accessTokenMaxAge
) {
}
