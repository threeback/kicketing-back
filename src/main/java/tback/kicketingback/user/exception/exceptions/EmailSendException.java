package tback.kicketingback.user.exception.exceptions;

public class EmailSendException extends RuntimeException {
	public EmailSendException() {
		super("회원가입 이메일 검증 코드 전송에 실패했습니다.");
	}
}
