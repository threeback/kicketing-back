package tback.kicketingback.performance.dto;

import java.time.LocalDate;

public record OnStageDTO(
	Long id,
	LocalDate dateTime,
	int round
) {
}