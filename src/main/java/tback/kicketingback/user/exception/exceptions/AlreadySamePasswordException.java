package tback.kicketingback.user.exception.exceptions;

public class AlreadySamePasswordException extends RuntimeException {

	public AlreadySamePasswordException() {
		super("기존 비밀번호와 동일");
	}
}
