package tback.kicketingback.performance.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.service.ReservationService;

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
}
