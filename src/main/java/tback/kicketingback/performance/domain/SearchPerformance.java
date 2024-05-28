package tback.kicketingback.performance.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.Getter;

@Document(indexName = "performance_index")
@Getter
public class SearchPerformance {

	@Id
	private String id;

	@Field(name = "name", type = FieldType.Text)
	private String name;

	@Field(name = "genre", type = FieldType.Text)
	private String genre;

	@Field(name = "imageUrl", type = FieldType.Text)
	private String imageUrl;

	@Field(name = "startDate", type = FieldType.Date)
	private String startDate;

	@Field(name = "endDate", type = FieldType.Date)
	private String endDate;

	@Field(name = "placeName", type = FieldType.Text)
	private String placeName;

	@Field(name = "placeHall", type = FieldType.Text)
	private String placeHall;
}