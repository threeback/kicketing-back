package tback.kicketingback.user.dto.response;

import java.util.List;

import tback.kicketingback.performance.dto.OnStageDTO;
import tback.kicketingback.performance.dto.PlaceDTO;
import tback.kicketingback.performance.dto.SeatDTO;
import tback.kicketingback.performance.dto.SimplePerformanceDTO;
import tback.kicketingback.performance.dto.SimpleReservationDTO;

public record MyReservationsInfoResponse(
	List<SeatDTO> seatDTOS,
	SimpleReservationDTO simpleReservationDTO,
	OnStageDTO onStageDTO,
	SimplePerformanceDTO simplePerformanceDTO,
	PlaceDTO placeDTO
) {
}
