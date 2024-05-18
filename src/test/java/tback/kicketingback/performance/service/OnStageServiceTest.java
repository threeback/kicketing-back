package tback.kicketingback.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.dto.GetBookableDatesRequest;
import tback.kicketingback.performance.dto.SimpleOnStageDTO;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceUUIDException;

@SpringBootTest
public class OnStageServiceTest {

	@Autowired
	private OnStageService onStageService;

	@Test
	@DisplayName("[실패] 존재하지 않는 공연 ID로 조회 시 예외 발생")
	void getUnorderedReservationsCountByGrade() {
		LocalDate startDate = LocalDate.of(2024, 5, 1);
		assertThatThrownBy(() -> onStageService.getBookableDates(UUID.randomUUID(),new GetBookableDatesRequest(startDate)))
			.isInstanceOf(InvalidPerformanceUUIDException.class);
	}

	@Test
	@DisplayName("[정상] UUID로 예매 가능한 공연 날짜 제공")
	void getBookableDates() {
		UUID performanceUUID = UUID.fromString("069939e9-dfed-46b5-832e-996cd737584a");
		LocalDate startDate = LocalDate.of(2024, 5, 1);
		LocalDate endDate = LocalDate.of(2024, 5, 31);
		List<SimpleOnStageDTO> simpleOnStageDTOS = onStageService.getBookableDates(performanceUUID, new GetBookableDatesRequest(startDate));

		for (SimpleOnStageDTO dto : simpleOnStageDTOS) {
			assertThat(dto.dateTime()).isAfterOrEqualTo(startDate.atStartOfDay());
			assertThat(dto.dateTime()).isBeforeOrEqualTo(endDate.atStartOfDay());
		}
	}
}
