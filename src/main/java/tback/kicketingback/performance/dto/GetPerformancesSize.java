package tback.kicketingback.performance.dto;

import lombok.Getter;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceSizeException;

@Getter
public class GetPerformancesSize {

	private final int size;

	public static GetPerformancesSize of(int size) {
		return new GetPerformancesSize(size);
	}

	private GetPerformancesSize(int size) {
		validatePerformanceSize(size);
		this.size = size;
	}

	private void validatePerformanceSize(int size) {
		if (10 <= size && size <= 50) {
			throw new InvalidGetPerformanceSizeException(size);
		}
	}
}
