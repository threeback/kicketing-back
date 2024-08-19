package tback.kicketingback.performance.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tback.kicketingback.performance.domain.PerformanceAutoComplete;
import tback.kicketingback.performance.domain.SearchPerformance;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.repository.PerformanceRepository;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final PerformanceRepository performanceRepository;

	public List<SearchPerformance> searchByName(String name, List<Genre> genres) {

		if (genres.contains(Genre.NONE)) {
			return performanceRepository.findByNameContaining(name)
				.stream()
				.map(SearchPerformance::from)
				.toList();
		}

		return performanceRepository.findByNameContainingAndGenreIn(name, genres)
			.stream()
			.map(SearchPerformance::from)
			.toList();
	}

	public List<PerformanceAutoComplete> searchByNameWithAutocomplete(String name) {
		return performanceRepository.findByNameContaining(name)
			.stream()
			.map(PerformanceAutoComplete::from)
			.toList();
	}
}
