package tback.kicketingback.performance.service;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.dto.GetPerformancesRequest;
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

		Assertions.assertThat(simplePerformancePlaceDTOS.size()).isGreaterThanOrEqualTo(0);
	}

	@ParameterizedTest
	@ValueSource(strings = {"NONE", "CONCERT", "MUSICAL", "CLASSIC", "THEATER"})
	@DisplayName("[정상] 장르를 선택해 검색")
	void getPerformancesByGenreName(String GenreName) {
		List<SimplePerformancePlaceDTO> simplePerformancePlaceDTOS = performanceService.getPerformances(GenreName,
			new GetPerformancesRequest("day", 10), LocalDate.now());

		Assertions.assertThat(simplePerformancePlaceDTOS.size()).isGreaterThanOrEqualTo(0);
	}
}
