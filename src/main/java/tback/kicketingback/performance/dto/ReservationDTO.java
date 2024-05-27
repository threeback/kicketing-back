package tback.kicketingback.performance.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import tback.kicketingback.performance.domain.Reservation;
import tback.kicketingback.performance.domain.type.DiscountType;

@Getter
public class ReservationDTO {
	private final UUID id;
	private final Long userId;
	private final Long onStageId;
	private final Long seatId;
	private final LocalDateTime orderedAt;
	private final String orderNumber;
	private final DiscountType discountType;
	private final LocalDateTime lockExpiredTime;

	private ReservationDTO(UUID id, Long userId, Long onStageId, Long seatId, LocalDateTime orderedAt,
		String orderNumber, DiscountType discountType, LocalDateTime lockExpiredTime) {
		this.id = id;
		this.userId = userId;
		this.onStageId = onStageId;
		this.seatId = seatId;
		this.orderedAt = orderedAt;
		this.orderNumber = orderNumber;
		this.discountType = discountType;
		this.lockExpiredTime = lockExpiredTime;
	}

	public static ReservationDTO of(Reservation reservation) {
		return new ReservationDTO(
			reservation.getId(),
			reservation.getUser().getId(),
			reservation.getOnStage().getId(),
			reservation.getSeat().getId(),
			reservation.getOrderedAt(),
			reservation.getOrderNumber(),
			reservation.getDiscountType(),
			reservation.getLockExpiredTime());
	}
}
