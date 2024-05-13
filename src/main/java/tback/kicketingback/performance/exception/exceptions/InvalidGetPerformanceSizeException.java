package tback.kicketingback.performance.exception.exceptions;

public class InvalidGetPerformanceSizeException extends RuntimeException {
	public InvalidGetPerformanceSizeException(int size) {
		super("%d: 유효하지 않은 검색 개수 [범위 초과]".formatted(size));
	}
}
