package tback.kicketingback.performance.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;
import tback.kicketingback.performance.domain.PerformanceAutoComplete;

public interface PerformanceAutoCompleteRepository extends ElasticsearchRepository<PerformanceAutoComplete, String> {

	@Query("{\"bool\": {\"must\": [{\"match_phrase_prefix\": {\"name\": {\"query\": \"?0\"}}}],"
		+ " \"filter\": [{\"range\": {\"endDate\": {\"gte\": \"now\"}}}]}}")
	List<PerformanceAutoComplete> findByNameWithAutocomplete(String name);
}
