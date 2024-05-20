package tback.kicketingback.performance.dto;

import tback.kicketingback.performance.domain.type.Grade;

public record SeatDTO(
	Grade grade,
	String seatRow,
	int seatCol
) {
}
