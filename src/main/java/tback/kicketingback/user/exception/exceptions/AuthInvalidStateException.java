package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidStateException extends RuntimeException {

    public AuthInvalidStateException() {
        super("변경 권한이 없음");
    }
}
