package ukathon.mayo.exception;

public class CustomTokenException extends CustomException {
    public CustomTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
