package tback.kicketingback.performance.dto;

import java.time.LocalDateTime;

public record SimpleOnStageDTO(
	Long id,
	LocalDateTime dateTime,
	int round
) {
}