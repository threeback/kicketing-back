package tback.kicketingback.performance.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.performance.domain.type.DiscountType;
import tback.kicketingback.performance.dto.CompleteReservationRequest;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.SelectSeatsRequest;
import tback.kicketingback.performance.service.ReservationService;
import tback.kicketingback.user.domain.User;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Validated
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping("/{uuid}/{onStageId}")
	public ResponseEntity<GetSeatInfoResponse> getSeatInfo(
		@PathVariable("uuid") UUID performanceUUID,
		@PathVariable("onStageId") Long onStageId
	) {
		GetSeatInfoResponse seatInfo = reservationService.getSeatInfo(performanceUUID, onStageId);

		return ResponseEntity.ok(seatInfo);
	}

	@PostMapping("/{onStageId}")
	public ResponseEntity<Void> lockOnStageSeats(
		@JwtLogin User user,
		@PathVariable("onStageId") Long onStageId,
		@RequestBody @Valid SelectSeatsRequest lockOnStageSeatsRequest
	) {
		reservationService.lockSeats(onStageId, lockOnStageSeatsRequest.getSeatIds(), user);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{onStageId}/{orderNumber}")
	public ResponseEntity<Void> completeReservation(
		@JwtLogin User user,
		@PathVariable("onStageId") Long onStageId,
		@PathVariable("orderNumber") String orderNumber,
		@RequestBody @Valid CompleteReservationRequest completeReservationRequest
	) {
		DiscountType discountType = DiscountType.of(completeReservationRequest.discountType());

		reservationService.completeReservation(
			onStageId,
			orderNumber,
			discountType,
			completeReservationRequest.seatsRequest().getSeatIds(),
			user);

		return ResponseEntity.ok().build();
	}
}
