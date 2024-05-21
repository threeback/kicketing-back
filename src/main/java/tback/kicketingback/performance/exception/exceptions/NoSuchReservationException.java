package tback.kicketingback.performance.exception.exceptions;

public class NoSuchReservationException extends RuntimeException {
	public NoSuchReservationException() {
		super("확인된 예약 없음");
	}
}