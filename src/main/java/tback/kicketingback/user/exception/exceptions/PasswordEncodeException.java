package tback.kicketingback.user.exception.exceptions;

public class PasswordEncodeException extends RuntimeException {

	public PasswordEncodeException() {
		super("비밀번호 암호화 실패");
	}
}
