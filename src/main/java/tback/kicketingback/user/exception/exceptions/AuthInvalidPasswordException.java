package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidPasswordException extends RuntimeException {

	public AuthInvalidPasswordException() {
		super("유효하지 않은 비밀번호");
	}
}
