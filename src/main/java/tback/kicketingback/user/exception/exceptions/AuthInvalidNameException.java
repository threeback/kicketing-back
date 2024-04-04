package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidNameException extends RuntimeException {

	public AuthInvalidNameException() {
		super("유효한 이름이 아닙니다.");
	}
}
