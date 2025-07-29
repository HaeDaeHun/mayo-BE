package ukathon.mayo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    INVALID_INPUT(400, "유효하지 않은 요청입니다."),
    INVALID_ENUM_VALUE(400, "enum 값이 잘못되었습니다."),
    INVALID_INPUT_VALUE(400, "입력값이 잘못되었습니다."),
    MISSING_PARAMETER(400, "필수 파라미터가 누락되었습니다."),

    // 401 Unauthorized
    UNAUTHORIZED(401, "인증이 필요합니다."),
    OAUTH2_LOGIN_FAILED(401, "소셜 로그인에 실패했습니다."),
    // 유효하지 않은 토큰
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),
    // 만료된 토큰
    EXPIRED_ACCESS_TOKEN(401,"만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, "만료된 리프레시 토큰입니다."),
    // 액세스 토큰이 만료되지 않은 상황에서 재발급받으려는 경우
    ACCESS_TOKEN_NOT_EXPIRED(401,"액세스 토큰이 아직 만료되지 않았습니다."),
    // 쿠키에 리프레시 토큰이 들어있지 않은 경우
    NO_COOKIE(401, "쿠키에 리프레시 토큰이 존재하지 않습니다."),

    // 403 Forbidden
    FORBIDDEN(403, "권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    BUSINESS_PROFILE_NOT_FOUND(404, "사업자 프로필을 찾을 수 없습니다."),
    MARKETER_PROFILE_NOT_FOUND(404, "마케터 프로필을 찾을 수 없습니다."),
    APPLICATION_NOT_FOUND(404, "신청 정보를 찾을 수 없습니다."),
    REFERENCE_NOT_FOUND(404, "레퍼런스를 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(404, "채팅 메시지를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없습니다."),

    // 409 Conflict
    ALREADY_EXISTS(409, "리소스가 이미 존재합니다."),

    // 500 Internal Server Error
    INTERNAL_ERROR(500, "서버 내부 오류입니다.");

    private final int status;
    private final String message;
}