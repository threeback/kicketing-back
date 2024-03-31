package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidEmailException extends RuntimeException {

	public AuthInvalidEmailException() {
		super("유효한 이메일이 아닙니다.");
	}
}
