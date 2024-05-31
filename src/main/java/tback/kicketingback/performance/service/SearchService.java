package tback.kicketingback.performance.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import tback.kicketingback.performance.domain.PerformanceAutoComplete;
import tback.kicketingback.performance.domain.SearchPerformance;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.repository.PerformanceAutoCompleteRepository;
import tback.kicketingback.performance.repository.SearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchRepository searchRepository;
	private final PerformanceAutoCompleteRepository performanceAutoCompleteRepository;

	public List<SearchPerformance> searchByName(String name, List<Genre> genres) {

		if (genres.contains(Genre.NONE)) {
			return searchRepository.findByNameWithoutGenre(name);
		}

		List<String> genreNames = genres.stream()
			.map(Genre::getValue)
			.toList();
		return searchRepository.findByNameAndGenres(name, genreNames);
	}

	public List<PerformanceAutoComplete> searchByNameWithAutocomplete(String name) {
		return performanceAutoCompleteRepository.findByNameWithAutocomplete(name);
	}
}