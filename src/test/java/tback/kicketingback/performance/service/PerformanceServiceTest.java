package tback.kicketingback.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.dto.GetBookableDatesRequest;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.SimpleOnStageDTO;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;

@SpringBootTest
class PerformanceServiceTest {

	@Autowired
	private PerformanceService performanceService;

	@Test
	@DisplayName("[정상] 장르가 NONE이면 장르를 가리지 않고 검색")
	void getPerformancesByNone() {
		List<SimplePerformancePlaceDTO> simplePerformancePlaceDTOS = performanceService.getPerformances("none",
			new GetPerformancesRequest("day", 10), LocalDate.now());

		assertThat(simplePerformancePlaceDTOS.size()).isGreaterThanOrEqualTo(0);
	}

	@ParameterizedTest
	@ValueSource(strings = {"NONE", "CONCERT", "MUSICAL", "CLASSIC", "THEATER"})
	@DisplayName("[정상] 장르를 선택해 검색")
	void getPerformancesByGenreName(String GenreName) {
		List<SimplePerformancePlaceDTO> simplePerformancePlaceDTOS = performanceService.getPerformances(GenreName,
			new GetPerformancesRequest("day", 10), LocalDate.now());

		assertThat(simplePerformancePlaceDTOS.size()).isGreaterThanOrEqualTo(0);
	}

	@Test
	@DisplayName("[정상] UUID로 예매 가능한 공연 날짜 제공")
	void getBookableDates() {
		UUID performanceUUID = UUID.fromString("069939e9-dfed-46b5-832e-996cd737584a");
		LocalDate startDate = LocalDate.of(2024, 5, 1);
		LocalDate endDate = LocalDate.of(2024, 5, 31);
		List<SimpleOnStageDTO> simpleOnStageDTOS = performanceService.getBookableDates(performanceUUID, new GetBookableDatesRequest(startDate));

		for (SimpleOnStageDTO dto : simpleOnStageDTOS) {
			assertThat(dto.dateTime()).isAfterOrEqualTo(startDate);
			assertThat(dto.dateTime()).isBeforeOrEqualTo(endDate);
		}
	}
}

