package tback.kicketingback.performance.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PerformanceDTO(
	UUID id,
	String name,
	String genre,
	int length,
	LocalDate startDate,
	LocalDate endDate,
	int ageLimit,
	String imageUrl,
	Long placeId
) {
}
