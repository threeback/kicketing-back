package tback.kicketingback.user.exception.exceptions;

public class EmailFormatException extends RuntimeException {

	public EmailFormatException() {
		super("유효하지 않은 이메일 형식");
	}
}
