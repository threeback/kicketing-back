package tback.kicketingback.performance.dto;

import tback.kicketingback.performance.domain.type.Grade;

public record SimpleSeatDTO(
	Long id,
	Grade grade,
	String seatRow,
	int seatCol
) {
}
