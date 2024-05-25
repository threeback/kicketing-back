package tback.kicketingback.performance.exception.exceptions;

public class PaymentServerErrorException extends RuntimeException {

	public PaymentServerErrorException() {
		super("결제에 실패 했습니다.");
	}
}
