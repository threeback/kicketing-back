package tback.kicketingback.performance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.Performance;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.GetPerformancesResponse;
import tback.kicketingback.performance.service.PerformanceService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PerformanceController {

	private final PerformanceService performanceService;

	/**
	 * @param genre(path variable): none[장르 지정 안함], concert, musical, classic, theater
	 * @param getPerformancesRequest(query string): dateUnit[day|week|month], size[10, 20, 50 개수]
	 * @return
	 */
	@GetMapping("/performances/{genre}")
	public ResponseEntity<GetPerformancesResponse> getPerformances(
		@PathVariable("genre") String genre,
		@Valid @ModelAttribute GetPerformancesRequest getPerformancesRequest
	) {
		List<Performance> performances = performanceService.getPerformances(genre, getPerformancesRequest);

		return ResponseEntity.ok().body(new GetPerformancesResponse(performances));
	}
}
