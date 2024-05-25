package tback.kicketingback.performance.exception.exceptions;

public class PaymentCancelServerErrorException extends RuntimeException {

	public PaymentCancelServerErrorException() {
		super("결제 취소에 실패 했습니다.");
	}
}
