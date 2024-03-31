package tback.kicketingback.user.exception.exceptions;

public class AuthInvalidPasswordException extends RuntimeException {

	public AuthInvalidPasswordException() {
		super("유효한 비밀번호가 아닙니다.");
	}
}
