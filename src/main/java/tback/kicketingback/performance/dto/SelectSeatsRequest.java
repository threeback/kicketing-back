package tback.kicketingback.performance.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tback.kicketingback.performance.exception.exceptions.DuplicateSeatSelectionException;

public record SelectSeatsRequest(List<Long> seatIds) {

	public SelectSeatsRequest(
		@NotNull(message = "좌석을 선택하지 않음")
		@Size(min = 1, max = 10, message = "좌석 범위 넘음 [1 ~10개 가능]")
		List<Long> seatIds
	) {
		validateSeatIds(seatIds);
		this.seatIds = seatIds;
	}

	private void validateSeatIds(List<Long> seatIds) {
		long count = seatIds.stream()
			.distinct()
			.count();

		if (count != seatIds.size()) {
			throw new DuplicateSeatSelectionException();
		}
	}
}
