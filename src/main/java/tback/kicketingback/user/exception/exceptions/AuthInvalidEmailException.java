package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidEmailException extends RuntimeException {

	public AuthInvalidEmailException() {
		super("유효하지 않은 이메일");
	}
}
