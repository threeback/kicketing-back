package tback.kicketingback.performance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.CanceledReservation;
import tback.kicketingback.performance.domain.OnStage;
import tback.kicketingback.performance.domain.Performance;
import tback.kicketingback.performance.domain.Place;
import tback.kicketingback.performance.domain.Reservation;
import tback.kicketingback.performance.domain.Seat;
import tback.kicketingback.performance.domain.type.DiscountType;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.ReservationDTO;
import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.dto.SeatGradeDTO;
import tback.kicketingback.performance.dto.SeatReservationDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceException;
import tback.kicketingback.performance.exception.exceptions.InvalidReservationDataException;
import tback.kicketingback.performance.exception.exceptions.InvalidSeatIdException;
import tback.kicketingback.performance.exception.exceptions.NoAvailableSeatsException;
import tback.kicketingback.performance.exception.exceptions.NoSuchReservationException;
import tback.kicketingback.performance.exception.exceptions.UnableCancelException;
import tback.kicketingback.performance.repository.CanceledReservationRepository;
import tback.kicketingback.performance.repository.OnStageRepository;
import tback.kicketingback.performance.repository.PerformanceRepository;
import tback.kicketingback.performance.repository.PerformanceRepositoryCustom;
import tback.kicketingback.performance.repository.PlaceRepository;
import tback.kicketingback.performance.repository.ReservationRepository;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.performance.repository.SeatGradeRepository;
import tback.kicketingback.performance.repository.SeatRepository;
import tback.kicketingback.user.domain.User;

@Service
@RequiredArgsConstructor
public class ReservationService {

	@Value("${reservation-policy.lock-time}")
	private int lockTime;

	@Value("${reservation-policy.free-lock-time.range.start}")
	private int freeLockTimeStart;
	@Value("${reservation-policy.free-lock-time.range.end}")
	private int freeLockTimeEnd;

	@Value("${reservation-policy.cancellable-time}")
	private int cancellableTime;

	private final ReservationRepository reservationRepository;
	private final OnStageRepository onStageRepository;
	private final PerformanceRepository performanceRepository;
	private final PlaceRepository placeRepository;
	private final SeatGradeRepository seatGradeRepository;
	private final SeatRepository seatRepository;
	private final CanceledReservationRepository canceledReservationRepository;
	private final ReservationRepositoryCustom reservationRepositoryCustom;
	private final PerformanceRepositoryCustom performanceRepositoryCustom;
	private final PaymentService paymentService;
	private final Random random = new Random();

	public GetSeatInfoResponse getSeatInfo(UUID performanceUUID, Long onStageId) {
		if (!performanceRepositoryCustom.isExistPerformance(performanceUUID, onStageId)) {
			throw new InvalidPerformanceException();
		}
		List<SimpleSeatDTO> onStageSeats = reservationRepositoryCustom.findOnStageSeats(onStageId)
			.orElseThrow(NoAvailableSeatsException::new);

		List<SeatGradeDTO> seatGradeDTOS = seatGradeRepository.findSeatGradesByPerformanceId(performanceUUID)
			.stream()
			.map(seatGrade -> new SeatGradeDTO(seatGrade.getId(), seatGrade.getGrade(), seatGrade.getPrice()))
			.toList();

		return new GetSeatInfoResponse(onStageSeats, seatGradeDTOS);
	}

	@Transactional(readOnly = true)
	public List<SeatGradeCount> getUnorderedReservationsCountByGrade(Long onStageId) {
		return reservationRepositoryCustom.getUnorderedReservationsCountByGrade(onStageId)
			.orElseThrow(() -> new InvalidOnStageIDException(onStageId));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void lockSeats(Long onStageId, List<Long> seatIds, User user) {
		List<SeatReservationDTO> seatReservationDTOS = getSeatReservationDTOS(onStageId, seatIds);
		checkSelected(seatReservationDTOS, user);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setUser(user);
			seatReservationDTO.reservation().setLockExpiredTime(LocalDateTime.now().plusMinutes(lockTime));
		});
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void completeReservation(Long onStageId, String orderNumber, DiscountType discountType, List<Long> seatIds,
		User user) {
		List<SeatReservationDTO> seatReservationDTOS = getSeatReservationDTOS(onStageId, seatIds);
		checkMySeats(user, seatReservationDTOS);

		//todo 내가 계산한 금액이랑 같이 보내서 검증해야됨
		paymentService.verifyPayment(orderNumber);

		seatReservationDTOS.forEach(seatReservationDTO -> {
			seatReservationDTO.reservation().setOrderedAt(LocalDateTime.now());
			seatReservationDTO.reservation().setOrderNumber(orderNumber);
			seatReservationDTO.reservation().setDiscountType(discountType);
		});
	}

	@Transactional
	public void cancelReservation(User user, String orderNumber) {
		List<Reservation> reservations = getReservations(orderNumber);
		List<ReservationDTO> reservationDTOS = reservations.stream().map(ReservationDTO::of).toList();

		checkReservedUser(user.getId(), reservationDTOS);
		checkSameOnStage(reservationDTOS);

		OnStage onStage = onStageRepository.findById(reservationDTOS.get(0).getOnStageId()).get();
		checkCancellable(onStage);

		paymentService.cancelPayment(orderNumber);

		Performance performance = performanceRepository.findById(onStage.getPerformance().getId()).get();
		Place place = placeRepository.findById(performance.getPlace().getId()).get();
		reservationDTOS.stream().map(reservationDTO -> {
			Seat seat = seatRepository.findById(reservationDTO.getSeatId()).get();
			return CanceledReservation.of(reservationDTO, user, performance.getName(), onStage, place, seat);
		}).forEach(canceledReservationRepository::save);

		reservations.forEach(reservation -> {
			reservation.setOrderedAt(null);
			reservation.setOrderNumber(null);
			reservation.setDiscountType(null);
			reservation.setLockExpiredTime(getRandomFreeLockTime());
		});
	}

	private List<Reservation> getReservations(String orderNumber) {
		List<Reservation> reservations = reservationRepository.findReservationByOrderNumber(orderNumber);
		if (reservations.isEmpty()) {
			throw new NoSuchReservationException();
		}
		return reservations;
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
			.filter(seatReservationDTO -> seatReservationDTO.reservation().getOrderNumber() != null || (
				seatReservationDTO.reservation().getUser().getId() != null && (
					seatReservationDTO.reservation().getLockExpiredTime() != null && seatReservationDTO.reservation()
						.getLockExpiredTime()
						.isAfter(LocalDateTime.now()))))
			.map(SeatReservationDTO::seat)
			.toList();
		if (!reservedSeats.isEmpty()) {
			throw AlreadySelectedSeatException.of(reservedSeats);
		}
	}

	private void checkMySeats(User user, List<SeatReservationDTO> seatReservationDTOS) {
		List<Seat> mySeats = seatReservationDTOS.stream()
			.filter(seatReservationDTO -> seatReservationDTO.reservation().getOrderNumber() == null
				&& seatReservationDTO.reservation().getUser().getId() != null && seatReservationDTO.reservation()
				.getUser()
				.getId()
				.equals(user.getId()) && seatReservationDTO.reservation().getLockExpiredTime() != null
				&& seatReservationDTO.reservation().getLockExpiredTime().isAfter(LocalDateTime.now()))
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

	private void checkReservedUser(Long userid, List<ReservationDTO> reservations) {
		long countNotUserReservation = reservations.stream()
			.filter(reservation -> !reservation.getUserId().equals(userid))
			.count();
		if (countNotUserReservation > 0) {
			throw new InvalidReservationDataException(reservations);
		}
	}

	private void checkSameOnStage(List<ReservationDTO> reservations) {
		long countOnStageId = reservations.stream().map(ReservationDTO::getOnStageId).distinct().count();
		if (countOnStageId != 1) {
			throw new InvalidReservationDataException(reservations);
		}
	}

	private void checkCancellable(OnStage onStage) {
		if (LocalDateTime.now().plusMinutes(cancellableTime).isBefore(onStage.getDateTime())) {
			throw new UnableCancelException();
		}
	}

	private LocalDateTime getRandomFreeLockTime() {
		return LocalDateTime.now().plusMinutes(random.ints(freeLockTimeStart, freeLockTimeEnd).findFirst().getAsInt());
	}
}
