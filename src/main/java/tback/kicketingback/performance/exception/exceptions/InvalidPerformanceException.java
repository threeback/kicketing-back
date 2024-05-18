package tback.kicketingback.performance.exception.exceptions;

public class InvalidPerformanceException extends RuntimeException {

	public InvalidPerformanceException() {
		super("존재하지 않는 공연");
	}
}
