package tback.kicketingback.performance.dto;

import java.util.List;

public record GetSeatInfoResponse(
	List<SimpleSeatDTO> bookableSeats,
	List<SimpleSeatDTO> unbookableSeats,
	List<SeatGradeDTO> seatGradeDTOS
) {
}
