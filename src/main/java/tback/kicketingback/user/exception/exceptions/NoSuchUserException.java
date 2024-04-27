package tback.kicketingback.user.exception.exceptions;

public class NoSuchUserException extends RuntimeException {

	public NoSuchUserException() {
		super("존재하지 않는 유저");
	}
}
