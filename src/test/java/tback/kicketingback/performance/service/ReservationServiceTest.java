package tback.kicketingback.performance.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.domain.type.DiscountType;
import tback.kicketingback.performance.dto.GetSeatInfoResponse;
import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceException;
import tback.kicketingback.performance.exception.exceptions.InvalidSeatIdException;
import tback.kicketingback.performance.exception.exceptions.NoAvailableSeatsException;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.repository.UserRepository;

@SpringBootTest
class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[예외] 존재 하지 않는 공연을 조회할 경우")
	void getUnorderedReservationsCountByGrade() {
		assertThatThrownBy(() -> reservationService.getUnorderedReservationsCountByGrade(0L))
			.isInstanceOf(InvalidOnStageIDException.class);
	}

	@Test
	@DisplayName("[정상] 예매 가능한 좌석수 제공")
	void getBookableSeats() {
		List<SeatGradeCount> unorderedReservationsCountByGrade = reservationService.getUnorderedReservationsCountByGrade(
			1L);

		assertThat(unorderedReservationsCountByGrade.size()).isGreaterThanOrEqualTo(0);
	}

	@Test
	@DisplayName("[실패] 공연 uuid와 OnStage Id가 연관이 없을 때")
	void getSeatInfoByMismatchPerformanceUUIDAndOnStageId() {
		UUID performanceUUID = UUID.fromString("069939e9-dfed-46b5-832e-996cd737584a");
		Long onStageId = 9L;

		assertThatThrownBy(() -> {
			reservationService.getSeatInfo(performanceUUID, onStageId);
		}).isInstanceOf(InvalidPerformanceException.class);
	}

	@Test
	@DisplayName("[실패] 예약 가능한 좌석이 없음")
	void getSeatInfoByNoSeat() {
		UUID performanceUUID = UUID.fromString("28f4caa7-84f8-4ff1-a4b7-ce0fc6e171aa");
		Long onStageId = 20L;

		assertThatThrownBy(() -> {
			reservationService.getSeatInfo(performanceUUID, onStageId);
		}).isInstanceOf(NoAvailableSeatsException.class);
	}

	@Test
	@DisplayName("[성공] 예약 가능한 좌석 정보 제공")
	void getSeatInfo() {
		UUID performanceUUID = UUID.fromString("0d6951b4-f86d-4707-a42c-53509774ffbc");
		Long onStageId = 9L;

		assertThatCode(() -> {
			GetSeatInfoResponse seatInfo = reservationService.getSeatInfo(performanceUUID, onStageId);
			System.out.println("seatInfo = " + seatInfo);
			assertThat(seatInfo.simpleSeatDTOS().size()).isGreaterThanOrEqualTo(1);
			assertThat(seatInfo.seatGradeDTOS().size()).isGreaterThanOrEqualTo(1);
		}).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("[실패] 유효하지 않은 좌석")
	void lockSeatsByInvalidSeatId() {
		Long onStageId = 1000L;
		List<Long> invalidSeatIds = List.of(10000L, 20000L, 30000L);
		User testUser = userRepository.findByEmail("test@test.com").get();

		assertThatThrownBy(() -> {
			reservationService.lockSeats(onStageId, invalidSeatIds, testUser);
		}).isInstanceOf(InvalidSeatIdException.class);
	}

	@Test
	@DisplayName("[실패] 이미 예약이 완료됐거나 점유 중인 좌석이 포함")
	void lockSeatsByAlreadySelectedSeat() {
		Long onStageId = 20L;
		List<Long> selectedSeatIds = List.of(298L, 299L, 300L);
		User testUser = userRepository.findByEmail("test@test.com").get();

		assertThatThrownBy(() -> {
			reservationService.lockSeats(onStageId, selectedSeatIds, testUser);
		}).isInstanceOf(AlreadySelectedSeatException.class);
	}

	@Test
	@DisplayName("[성공] 좌석 점유가 완료됨")
	void lockSeats() {
		Long onStageId = 9L;
		List<Long> selectedSeatIds = List.of(111L, 112L, 113L);
		User testUser = userRepository.findByEmail("test@test.com").get();

		assertThatCode(() -> {
			reservationService.lockSeats(onStageId, selectedSeatIds, testUser);
		}).doesNotThrowAnyException();

		UUID performanceUUID = UUID.fromString("0d6951b4-f86d-4707-a42c-53509774ffbc");
		assertThatThrownBy(() -> {
			reservationService.getSeatInfo(performanceUUID, onStageId);
		}).isInstanceOf(NoAvailableSeatsException.class);
	}

	// @Test
	// @DisplayName("[성공] 좌석 예매가 완료됨")
	// void completeReservation() {
	// 	Long onStageId = 1L;
	// 	List<Long> selectedSeatIds = List.of(1L, 2L, 3L);
	// 	String orderNumber = "testOrderNumber";
	// 	String testDiscountType = "NONE";
	// 	User testUser = userRepository.findByEmail("test@test.com").get();
	// 	DiscountType discountType = DiscountType.valueOf(testDiscountType);
	//
	// 	assertThatCode(() -> {
	// 		reservationService.lockSeats(onStageId, selectedSeatIds, testUser);
	// 		reservationService.completeReservation(onStageId, orderNumber, discountType, selectedSeatIds, testUser);
	// 	}).doesNotThrowAnyException();
	//
	// }

	@Test
	@DisplayName("[실패] 좌석 Lock 시간이 만료됨")
	void expiredLocking() {
		Long onStageId = 1L;
		List<Long> selectedSeatIds = List.of(4L, 5L);
		String orderNumber = "testOrderNumber";
		String testDiscountType = "NONE";
		User testUser = userRepository.findByEmail("test@test.com").get();
		DiscountType discountType = DiscountType.valueOf(testDiscountType);

		assertThrows(AlreadySelectedSeatException.class,
			() -> reservationService.completeReservation(onStageId, orderNumber, discountType, selectedSeatIds,
				testUser));
	}

	@Test
	@DisplayName("[실패] 좌석 예매는 안했지만 다른 유저가 Lock 을 걸음")
	void lockedSeat() {
		Long onStageId = 1L;
		List<Long> selectedSeatIds = List.of(4L, 5L);
		String orderNumber = "testOrderNumber";
		String testDiscountType = "NONE";
		User testUser = userRepository.findByEmail("pjeka7er43@naver.com").get();
		DiscountType discountType = DiscountType.valueOf(testDiscountType);

		assertThrows(AlreadySelectedSeatException.class,
			() -> reservationService.completeReservation(onStageId, orderNumber, discountType, selectedSeatIds,
				testUser));
	}

	@Test
	@DisplayName("[실패] 다른 고객이 예매한 좌석")
	void alreadyReservation() {
		Long onStageId = 1L;
		List<Long> selectedSeatIds = List.of(1L, 2L, 3L);
		String orderNumber = "testOrderNumber";
		String testDiscountType = "NONE";
		User testUser = userRepository.findByEmail("pjeka7er43@naver.com").get();
		DiscountType discountType = DiscountType.valueOf(testDiscountType);

		assertThrows(AlreadySelectedSeatException.class,
			() -> reservationService.completeReservation(onStageId, orderNumber, discountType, selectedSeatIds,
				testUser));
	}

	@Test
	@DisplayName("[실패] 존재하지 않는 좌석 예매")
	void doesntExistSeats() {
		Long onStageId = 1L;
		List<Long> selectedSeatIds = List.of(19999L, 299999L, 399999L);
		String orderNumber = "testOrderNumber";
		String testDiscountType = "NONE";
		User testUser = userRepository.findByEmail("test@test.com").get();
		DiscountType discountType = DiscountType.valueOf(testDiscountType);

		assertThrows(InvalidSeatIdException.class,
			() -> reservationService.completeReservation(onStageId, orderNumber, discountType, selectedSeatIds,
				testUser));
	}

}
