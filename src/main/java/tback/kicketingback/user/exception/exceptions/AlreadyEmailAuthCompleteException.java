package tback.kicketingback.user.exception.exceptions;

public class AlreadyEmailAuthCompleteException extends RuntimeException {

	public AlreadyEmailAuthCompleteException() {
		super("이미 인증이 완료된 이메일");
	}
}
