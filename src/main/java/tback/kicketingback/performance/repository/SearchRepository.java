package tback.kicketingback.performance.repository;

import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tback.kicketingback.performance.domain.SearchPerformance;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchPerformance, String> {

	List<SearchPerformance> findByName(String name);

	@Query("{\"match_phrase_prefix\": {\"name\": {\"query\": \"?0\"}}}")
	List<SearchPerformance> findByNameWithAutocomplete(String name);
}