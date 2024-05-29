package tback.kicketingback.performance.dto;

import java.util.Map;

import tback.kicketingback.user.dto.response.CanceledReservationDTO;

public record MyCanceledReservationResponse(Map<String, CanceledReservationDTO> canceledReservationDTOMap) {
}
