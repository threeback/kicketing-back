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
import tback.kicketingback.performance.domain.type.DiscountType;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.dto.SeatGradeDTO;
import tback.kicketingback.performance.dto.SeatReservationDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;
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
					seatGrade.getGrade(),
					seatGrade.getPrice()))
			.toList();

		return new GetSeatInfoResponse(onStageSeats, seatGradeDTOS);
	}

	@Transactional(readOnly = true)
	public List<SeatGradeCount> getUnorderedReservationsCountByGrade(Long onStageId) {
		return reservationRepositoryCustom.getUnorderedReservationsCountByGrade(onStageId)
			.orElseThrow(() -> new InvalidOnStageIDException(onStageId));
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void lockSeats(Long onStageId, List<Long> seatIds, User user) {
		List<SeatReservationDTO> seatReservationDTOS = getSeatReservationDTOS(onStageId, seatIds);
		checkSelected(seatReservationDTOS, user);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setUser(user);
			seatReservationDTO.reservation().setLockExpiredTime(LocalDateTime.now().plusMinutes(lockTime));
		});
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void completeReservation(
		Long onStageId,
		String orderNumber,
		DiscountType discountType,
		List<Long> seatIds,
		User user
	) {
		List<SeatReservationDTO> seatReservationDTOS = getSeatReservationDTOS(onStageId, seatIds);
		checkSelected(seatReservationDTOS, user);
		checkMySeats(user, seatReservationDTOS);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setOrderedAt(LocalDateTime.now());
			seatReservationDTO.reservation().setOrderNumber(orderNumber);
			seatReservationDTO.reservation().setDiscountType(discountType);
		});
	}

	public void validatePayment(String orderNumber, int price) {
		//todo 결제 api 로 오더 넘버랑 금액 보내서 유효한지 확인
	}

	private List<SeatReservationDTO> getSeatReservationDTOS(Long onStageId, List<Long> seatIds) {
		List<SeatReservationDTO> seatReservationDTOS = reservationRepositoryCustom.findSeats(onStageId, seatIds);

		if (seatIds.size() != seatReservationDTOS.size()) {
			throw new InvalidSeatIdException();
		}

		return seatReservationDTOS;
	}

	private void checkSelected(List<SeatReservationDTO> seatReservationDTOS, User user) {
		List<Seat> reservedSeats = seatReservationDTOS.stream()
			.filter(seatReservationDTO -> seatReservationDTO.reservation().getOrderNumber() != null ||
				(seatReservationDTO.reservation().getUser().getId() != null &&
					!seatReservationDTO.reservation().getUser().getId().equals(user.getId()) &&
					(seatReservationDTO.reservation().getLockExpiredTime() != null &&
						seatReservationDTO.reservation().getLockExpiredTime().isAfter(LocalDateTime.now()))))
			.map(SeatReservationDTO::seat)
			.toList();
		if (!reservedSeats.isEmpty()) {
			throw AlreadySelectedSeatException.of(reservedSeats);
		}
	}

	private void checkMySeats(User user, List<SeatReservationDTO> seatReservationDTOS) {
		List<Seat> mySeats = seatReservationDTOS.stream()
			.filter(seatReservationDTO ->
				seatReservationDTO.reservation().getUser().getId() != null &&
					seatReservationDTO.reservation().getUser().getId().equals(user.getId()) &&
					seatReservationDTO.reservation().getLockExpiredTime() != null &&
					seatReservationDTO.reservation().getLockExpiredTime().isAfter(LocalDateTime.now()))
			.map(SeatReservationDTO::seat)
			.toList();
		if (mySeats.size() != seatReservationDTOS.size()) {
			List<Seat> notMySeat = seatReservationDTOS.stream()
				.map(SeatReservationDTO::seat)
				.filter(seat -> !mySeats.contains(seat))
				.toList();
			throw AlreadySelectedSeatException.of(notMySeat);
		}
	}
}
