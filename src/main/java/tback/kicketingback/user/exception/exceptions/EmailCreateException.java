package tback.kicketingback.user.exception.exceptions;

public class EmailCreateException extends RuntimeException {
	public EmailCreateException() {
		super("회원가입 인증 이메일 생성 실패했습니다.");
	}
}
