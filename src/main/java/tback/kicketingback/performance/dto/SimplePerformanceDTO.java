package tback.kicketingback.performance.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SimplePerformanceDTO(
	UUID id,
	String genre,
	String name,
	LocalDate startDate,
	LocalDate endDate,
	String imageUrl
) {
}
