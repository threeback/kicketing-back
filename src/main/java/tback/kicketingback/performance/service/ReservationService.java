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
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceException;
import tback.kicketingback.performance.exception.exceptions.InvalidSeatIdException;
import tback.kicketingback.performance.exception.exceptions.NoAvailableSeatsException;
import tback.kicketingback.performance.repository.PerformanceRepositoryCustom;
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
	private final PerformanceRepositoryCustom performanceRepositoryCustom;

	public GetSeatInfoResponse getSeatInfo(UUID performanceUUID, Long onStageId) {
		if (!performanceRepositoryCustom.isExistPerformance(performanceUUID, onStageId)) {
			throw new InvalidPerformanceException();
		}
		List<SimpleSeatDTO> onStageSeats = reservationRepositoryCustom.findOnStageSeats(onStageId)
			.orElseThrow(NoAvailableSeatsException::new);

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
	public void lockSeats(Long onStageId, List<Long> seatIds, User user) {
		List<SeatReservationDTO> seatReservationDTOS = reservationRepositoryCustom.findSeats(onStageId, seatIds);
		System.out.println("seatReservationDTOS = " + seatReservationDTOS);

		if (seatIds.size() != seatReservationDTOS.size()) {
			throw new InvalidSeatIdException();
		}
		checkSelected(seatReservationDTOS);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setUser(user);
			seatReservationDTO.reservation().setLockExpiredTime(LocalDateTime.now().plusMinutes(lockTime));
		});
	}

	private void checkSelected(List<SeatReservationDTO> seatReservationDTOS) {
		List<Seat> reservedSeats = seatReservationDTOS.stream()
			.filter(seatReservationDTO -> seatReservationDTO.reservation().getOrderNumber() != null ||
				(seatReservationDTO.reservation().getUser().getId() != null &&
					(seatReservationDTO.reservation().getLockExpiredTime() != null &&
						seatReservationDTO.reservation().getLockExpiredTime().isAfter(LocalDateTime.now()))))
			.map(SeatReservationDTO::seat)
			.toList();
		if (!reservedSeats.isEmpty()) {
			throw AlreadySelectedSeatException.of(reservedSeats);
		}
	}
}
