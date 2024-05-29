package tback.kicketingback.performance.dto;

import jakarta.validation.constraints.NotNull;

public record CompleteReservationRequest(
	@NotNull
	String discountType,
	@NotNull
	SelectSeatsRequest seatsRequest
) {
}
