package tback.kicketingback.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import tback.kicketingback.performance.dto.SeatDTO;

public record CanceledReservationDTO(
	Long id,
	LocalDateTime canceledAt,
	LocalDateTime orderedAt,
	String performanceName,
	LocalDateTime performanceDate,
	int round,
	String placeName,
	String hall,
	List<SeatDTO> seatDTOS
) {
}
