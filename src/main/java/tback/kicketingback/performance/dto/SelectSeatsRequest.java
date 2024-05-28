package tback.kicketingback.performance.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tback.kicketingback.performance.exception.exceptions.DuplicateSeatSelectionException;

@Getter
@NoArgsConstructor
public class SelectSeatsRequest {

	@NotNull(message = "좌석을 선택하지 않음")
	@Size(min = 1, max = 10, message = "좌석 범위 넘음 [1 ~10개 가능]")
	private List<Long> seatIds;

	public void setSeatIds(List<Long> seatIds) {
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
