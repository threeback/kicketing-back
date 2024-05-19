package tback.kicketingback.performance.exception.exceptions;

public class NoAvailableSeatsException extends RuntimeException {

	public NoAvailableSeatsException() {
		super("예매 가능한 좌석이 없음");
	}
}
