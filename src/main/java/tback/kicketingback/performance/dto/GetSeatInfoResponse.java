package tback.kicketingback.performance.dto;

import java.util.List;

public record GetSeatInfoResponse(
	List<SimpleSeatDTO> simpleSeatDTOS,
	List<SeatGradeDTO> seatGradeDTOS
) {
}
