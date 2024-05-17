package tback.kicketingback.performance.dto;

import java.util.UUID;

import tback.kicketingback.performance.domain.type.Grade;

public record SeatGradeDTO(
	Long id,
	UUID performance_id,
	Grade grade,
	int price
) {
}
