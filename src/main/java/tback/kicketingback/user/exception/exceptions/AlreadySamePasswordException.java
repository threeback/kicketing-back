package tback.kicketingback.user.exception.exceptions;

public class AlreadySamePasswordException extends RuntimeException {

	public AlreadySamePasswordException() {
		super("이전 비밀번호와 동일합니다.");
	}
}
