package tback.kicketingback.performance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.PerformanceAutoComplete;
import tback.kicketingback.performance.domain.SearchPerformance;
import tback.kicketingback.performance.dto.PerformanceAutoCompleteResponse;
import tback.kicketingback.performance.dto.PerformanceSearchRequest;
import tback.kicketingback.performance.dto.PerformanceSearchResponse;
import tback.kicketingback.performance.service.SearchService;

@RequestMapping("/api/search")
@RestController
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@GetMapping("/performances")
	public ResponseEntity<PerformanceSearchResponse> searchPerformances(
		@ModelAttribute @Valid PerformanceSearchRequest request) {
		List<SearchPerformance> performances = searchService.searchByName(request.name(), request.genres());
		return ResponseEntity.ok()
			.body(PerformanceSearchResponse.from(performances));
	}

	@GetMapping("/performances/autocomplete")
	public ResponseEntity<PerformanceAutoCompleteResponse> autocompletePerformances(@RequestParam String name) {
		List<PerformanceAutoComplete> searchPerformances = searchService.searchByNameWithAutocomplete(name);
		return ResponseEntity.ok()
			.body(PerformanceAutoCompleteResponse.from(searchPerformances));
	}
}