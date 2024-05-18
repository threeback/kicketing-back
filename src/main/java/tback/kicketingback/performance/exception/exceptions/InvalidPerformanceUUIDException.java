package tback.kicketingback.performance.exception.exceptions;

import java.util.UUID;

public class InvalidPerformanceUUIDException extends RuntimeException {
	public InvalidPerformanceUUIDException(UUID performanceUUID) {
		super("유효하지 않은 공연 UUID: %s".formatted(performanceUUID));
	}
}
