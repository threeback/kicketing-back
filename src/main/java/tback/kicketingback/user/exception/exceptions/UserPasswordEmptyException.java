package tback.kicketingback.user.exception.exceptions;

public class UserPasswordEmptyException extends RuntimeException {

	public UserPasswordEmptyException() {
		super("비밀번호를 입력하세요");
	}
}
