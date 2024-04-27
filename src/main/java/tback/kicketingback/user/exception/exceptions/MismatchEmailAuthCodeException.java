package tback.kicketingback.user.exception.exceptions;

public class MismatchEmailAuthCodeException extends RuntimeException {

	public MismatchEmailAuthCodeException() {
		super("인증 번호가 틀림");
	}
}
