package tback.kicketingback.performance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.SearchPerformance;
import tback.kicketingback.performance.service.SearchService;

@RequestMapping("/api/search")
@RestController
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@GetMapping("/performances")
	public ResponseEntity<List<SearchPerformance>> searchPerformances(@RequestParam @Valid @Size(min = 2, message = "검색어는 2글자 이상이어야 합니다.") String name) {
		List<SearchPerformance> performances = searchService.searchByName(name);
		return ResponseEntity.ok()
			.body(performances);
	}

	@GetMapping("/performances/autocomplete")
	public ResponseEntity<Iterable<SearchPerformance>> autocompletePerformances(@RequestParam String name) {
		List<SearchPerformance> searchPerformances = searchService.searchByNameWithAutocomplete(name);
		return ResponseEntity.ok()
			.body(searchPerformances);
	}
}
