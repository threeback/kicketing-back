package tback.kicketingback.user.exception.exceptions;

public class EmailAuthIncompleteException extends RuntimeException {

	public EmailAuthIncompleteException() {
		super("이메일 인증이 완료되지 않음");
	}
}
