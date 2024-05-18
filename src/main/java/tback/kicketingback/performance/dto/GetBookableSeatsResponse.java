package tback.kicketingback.performance.dto;

import java.util.List;

public record GetBookableSeatsResponse(
	List<SeatGradeCount> seatGradeCounts
) {
}
