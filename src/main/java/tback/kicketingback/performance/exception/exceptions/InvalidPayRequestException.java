package tback.kicketingback.performance.exception.exceptions;

public class InvalidPayRequestException extends RuntimeException {

	public InvalidPayRequestException() {
		super("잘못된 결제 요청");
	}
}
