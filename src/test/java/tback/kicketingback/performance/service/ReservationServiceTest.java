package tback.kicketingback.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;

@SpringBootTest
public class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

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
}
