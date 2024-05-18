package tback.kicketingback.performance.exception.exceptions;

public class DuplicateSeatSelectionException extends RuntimeException {

	public DuplicateSeatSelectionException() {
		super("좌석 중복 선택");
	}
}
