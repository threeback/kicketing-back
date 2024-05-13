package tback.kicketingback.performance.exception.exceptions;

public class InvalidGetPerformanceDateUnitException extends RuntimeException {
	public InvalidGetPerformanceDateUnitException(String duration) {
		super("%s: 유효하지 않은 공연 검색 기간".formatted(duration));
	}
}
