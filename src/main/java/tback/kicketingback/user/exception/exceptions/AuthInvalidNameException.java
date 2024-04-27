package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidNameException extends RuntimeException {

	public AuthInvalidNameException() {
		super("유효하지 않은 이름");
	}
}
