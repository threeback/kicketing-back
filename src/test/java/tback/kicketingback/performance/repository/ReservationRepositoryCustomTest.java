package tback.kicketingback.performance.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReservationRepositoryCustomTest {

	@Autowired
	private ReservationRepositoryCustom reservationRepositoryCustom;

	@Test
	@DisplayName("예약을 하지 않았다면 리스트가 비어있다.")
	public void 예약이_없는_유저() {
		Long userId = 2L;
		assertThat(reservationRepositoryCustom.myReservations(userId).isEmpty());

	}

	@Test
	@DisplayName("예약을 한 유저는 예외를 던지지 않는다.")
	public void 예약을_한_유저() {
		Long userId = 1L;
		assertThat(reservationRepositoryCustom.myReservations(userId).isEmpty()).isFalse();
	}
}
