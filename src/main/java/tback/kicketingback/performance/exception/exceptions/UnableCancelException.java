package tback.kicketingback.performance.exception.exceptions;

public class UnableCancelException extends RuntimeException {

	public UnableCancelException() {
		super("취소 가능 시간 넘김");
	}
}
