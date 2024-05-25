package tback.kicketingback.performance.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SimpleReservationDTO(
	UUID id,
	LocalDateTime orderedAt,
	String orderNumber
) {
}
