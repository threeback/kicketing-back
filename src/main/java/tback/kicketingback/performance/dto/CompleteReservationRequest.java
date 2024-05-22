package tback.kicketingback.performance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CompleteReservationRequest(
	@NotNull
	String discountType,
	@Positive
	int price,
	@NotNull
	SelectSeatsRequest seatsRequest
) {
}
