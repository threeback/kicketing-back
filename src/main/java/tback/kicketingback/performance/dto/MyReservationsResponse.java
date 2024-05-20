package tback.kicketingback.performance.dto;

import java.util.List;

public record MyReservationsResponse(
	List<DetailReservationDTO> reservations
) {
}
