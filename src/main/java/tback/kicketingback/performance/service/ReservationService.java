package tback.kicketingback.performance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.Seat;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.SeatGradeDTO;
import tback.kicketingback.performance.dto.SeatReservationDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.performance.repository.SeatGradeRepository;
import tback.kicketingback.user.domain.User;

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

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void lockSeats(List<Long> seatIds, User user) {
		List<SeatReservationDTO> seatReservationDTOS = reservationRepositoryCustom.findSeats(seatIds);

		checkSelected(seatReservationDTOS);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setUser(user);
			seatReservationDTO.seat().setLockExpiredTime(LocalDateTime.now().plusMinutes(lockTime));
		});
	}

	private void checkSelected(List<SeatReservationDTO> seatReservationDTOS) {
		List<Seat> reservedSeats = seatReservationDTOS.stream()
			.filter(seatReservationDTO -> seatReservationDTO.reservation().getOrderNumber() != null ||
				(seatReservationDTO.seat().getLockExpiredTime().isAfter(LocalDateTime.now())))
			.map(SeatReservationDTO::seat)
			.toList();
		if (!reservedSeats.isEmpty()) {
			throw AlreadySelectedSeatException.of(reservedSeats);
		}
	}
}
