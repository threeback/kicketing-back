package tback.kicketingback.performance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tback.kicketingback.performance.domain.SearchPerformance;
import tback.kicketingback.performance.repository.SearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchRepository searchRepository;

	public List<SearchPerformance> searchByName(String name) {
		return searchRepository.findByName(name);
	}

	public List<SearchPerformance> searchByNameWithAutocomplete(String name) {
		return searchRepository.findByNameWithAutocomplete(name);
	}
}