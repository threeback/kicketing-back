package tback.kicketingback.performance.dto;

import tback.kicketingback.performance.domain.type.Grade;

public record SeatGradeDTO(
	Long id,
	Grade grade,
	int price
) {
}
