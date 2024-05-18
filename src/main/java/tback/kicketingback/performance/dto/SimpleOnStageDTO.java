package tback.kicketingback.performance.dto;

import java.time.LocalDate;

public record SimpleOnStageDTO(
	Long id,
	LocalDate dateTime,
	int round
) {
}