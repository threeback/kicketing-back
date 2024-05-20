package tback.kicketingback.performance.dto;

import java.time.LocalDateTime;

public record OnStageDTO(
	LocalDateTime dateTime,
	int round
) {
}
