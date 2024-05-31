package tback.kicketingback.performance.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.Getter;

@Document(indexName = "performance_index")
@Getter
public class PerformanceAutoComplete {

	@Id
	private String id;

	@Field(name = "name", type = FieldType.Text)
	private String name;

	@Field(name = "genre", type = FieldType.Text)
	private String genre;
}