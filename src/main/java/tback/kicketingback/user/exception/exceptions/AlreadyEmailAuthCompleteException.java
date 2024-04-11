package tback.kicketingback.user.exception.exceptions;

public class AlreadyEmailAuthCompleteException extends RuntimeException {

	public AlreadyEmailAuthCompleteException() {
		super("이미 이메일 인증이 완료되었습니다.");
	}
}
