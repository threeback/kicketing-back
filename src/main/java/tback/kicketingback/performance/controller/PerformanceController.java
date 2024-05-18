package tback.kicketingback.performance.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.dto.DetailPerformanceDTO;
import tback.kicketingback.performance.dto.GetBookableDatesRequest;
import tback.kicketingback.performance.dto.GetBookableDatesResponse;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.GetPerformancesResponse;
import tback.kicketingback.performance.dto.OnStageDTO;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;
import tback.kicketingback.performance.service.PerformanceService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class PerformanceController {

	private final PerformanceService performanceService;

	/**
	 * @param genre(path variable): none[장르 지정 안함], concert, musical, classic, theater
	 * @param getPerformancesRequest(query string): dateUnit[day|week|month], size[10, 20, 50 개수]
	 * @return
	 */
	@GetMapping("/performances/{genre}")
	public ResponseEntity<GetPerformancesResponse> getPerformances(@PathVariable("genre") String genre,
		@Valid @ModelAttribute GetPerformancesRequest getPerformancesRequest) {
		List<SimplePerformancePlaceDTO> simplePerformancePlaceDTOS =
			performanceService.getPerformances(genre, getPerformancesRequest, LocalDate.now());

		GetPerformancesResponse getPerformancesResponse = new GetPerformancesResponse(simplePerformancePlaceDTOS);
		return ResponseEntity.ok().body(getPerformancesResponse);
	}

	@GetMapping("/performance/{uuid}")
	public ResponseEntity<DetailPerformanceDTO> getPerformance(
		@PathVariable("uuid") UUID performanceUUID
	) {
		DetailPerformanceDTO performance = performanceService.getPerformance(performanceUUID);

		return ResponseEntity.ok(performance);
	}

	@GetMapping("/performance/{uuid}/bookable-dates")
	public ResponseEntity<?> getBookableDates(
		@PathVariable("uuid") UUID performanceUUID,
		@Valid @ModelAttribute GetBookableDatesRequest getBookableDatesRequest) {
		List<OnStageDTO> bookableDates = performanceService.getBookableDates(performanceUUID,
			getBookableDatesRequest);

		GetBookableDatesResponse getBookableDatesResponse = new GetBookableDatesResponse(bookableDates);
		return ResponseEntity.ok(getBookableDatesResponse);
	}
}
