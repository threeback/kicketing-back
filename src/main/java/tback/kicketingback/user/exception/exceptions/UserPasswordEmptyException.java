package tback.kicketingback.user.exception.exceptions;

public class UserPasswordEmptyException extends RuntimeException {

	public UserPasswordEmptyException() {
		super("비밀번호 공백");
	}
}
