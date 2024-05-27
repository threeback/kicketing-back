package tback.kicketingback.performance.exception.exceptions;

public class InvalidPayCancelRequestException extends RuntimeException {

	public InvalidPayCancelRequestException() {
		super("잘못된 결제 취소 요청");
	}
}
