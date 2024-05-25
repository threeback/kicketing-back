package tback.kicketingback.performance.dto;

import java.util.Map;

import tback.kicketingback.user.dto.response.MyReservationsInfoResponse;

public record MyReservationsResponse(
	Map<String, MyReservationsInfoResponse> reservations
) {
}
