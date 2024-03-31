package tback.kicketingback.user.exception.exceptions;

public class EmailFormatException extends RuntimeException {

	public EmailFormatException() {
		super("유효한 이메일 형식이 아닙니다.");
	}
}
