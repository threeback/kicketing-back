package tback.kicketingback.performance.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.SeatGradeDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.performance.repository.SeatGradeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

	@Value("${reservation-policy.lock-time}")
	private int lockTime;
	private final SeatGradeRepository seatGradeRepository;
	private final ReservationRepositoryCustom reservationRepositoryCustom;

	public GetSeatInfoResponse getSeatInfo(UUID performanceUUID, Long onStageId) {
		List<SimpleSeatDTO> onStageSeats = reservationRepositoryCustom.findOnStageSeats(onStageId);

		List<SeatGradeDTO> seatGradeDTOS = seatGradeRepository.findSeatGradesByPerformanceId(performanceUUID).stream()
			.map(seatGrade ->
				new SeatGradeDTO(seatGrade.getId(),
					seatGrade.getPerformance().getId(),
					seatGrade.getGrade(),
					seatGrade.getPrice()))
			.toList();

		return new GetSeatInfoResponse(onStageSeats, seatGradeDTOS);
	}
}
