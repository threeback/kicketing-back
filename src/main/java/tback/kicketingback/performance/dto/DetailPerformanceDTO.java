package tback.kicketingback.performance.dto;

import java.util.List;

public record DetailPerformanceDTO(
	PerformanceDTO performanceDTO,
	PlaceDTO placeDTO,
	List<SeatGradeDTO> seatGrades,
	List<StarDTO> stars
) {
}
