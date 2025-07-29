package ukathon.mayo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import ukathon.mayo.exception.ErrorCode;
import ukathon.mayo.exception.ErrorDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, String requestUri) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // ErrorDto 생성
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage(),
                requestUri
        );

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorDto);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
