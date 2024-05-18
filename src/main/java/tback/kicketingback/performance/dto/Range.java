package tback.kicketingback.performance.dto;

import java.time.LocalDate;

public record Range(
	LocalDate start,
	LocalDate end
) {
}
