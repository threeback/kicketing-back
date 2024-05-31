package tback.kicketingback.performance.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

import tback.kicketingback.performance.domain.SearchPerformance;

public interface SearchRepository extends ElasticsearchRepository<SearchPerformance, String> {

	@Query("{\"bool\": {\"must\": [{\"match_phrase_prefix\": {\"name\": \"?0\"}}], " +
		"\"filter\": [{\"terms\": {\"genre\": ?1}}, {\"range\": {\"endDate\": {\"gte\": \"now\"}}}]}}")
	List<SearchPerformance> findByNameAndGenres(String name, List<String> genres);

	@Query("{\"bool\": {\"must\": [{\"match_phrase_prefix\": {\"name\": \"?0\"}}], " +
		"\"filter\": [{\"range\": {\"endDate\": {\"gte\": \"now\"}}}]}}")
	List<SearchPerformance> findByNameWithoutGenre(String name);
}
