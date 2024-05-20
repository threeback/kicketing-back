package tback.kicketingback.performance.exception.exceptions;

public class InvalidSeatIdException extends RuntimeException {

	public InvalidSeatIdException() {
		super("유효하지 않은 좌석");
	}
}
