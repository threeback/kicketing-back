package tback.kicketingback.performance.dto;

import lombok.Getter;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceSizeException;

@Getter
public class GetPerformancesSize {

	private final int anInt;

	public static GetPerformancesSize of(int size) {
		return new GetPerformancesSize(size);
	}

	private GetPerformancesSize(int anInt) {
		validatePerformanceSize(anInt);
		this.anInt = anInt;
	}

	private void validatePerformanceSize(int size) {
		if (size < 10 || size > 50) {
			throw new InvalidGetPerformanceSizeException(size);
		}
	}
}
