package tback.kicketingback.performance.dto;

import jakarta.validation.constraints.Positive;

public record CompleteReservationRequest(
	String discountType,
	@Positive
	int price,
	SelectSeatsRequest seatsRequest
) {
}
