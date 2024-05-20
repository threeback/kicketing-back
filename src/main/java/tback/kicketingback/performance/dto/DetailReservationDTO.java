package tback.kicketingback.performance.dto;

public record DetailReservationDTO(
	SimpleReservationDTO simpleReservationDTO,
	OnStageDTO onStageDTO,
	SeatDTO seatDTO,
	SimplePerformanceDTO simplePerformanceDTO,
	PlaceDTO placeDTO
) {
}
