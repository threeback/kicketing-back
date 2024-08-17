package tback.kicketingback.performance.domain;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;

@Getter
public class SearchPerformance {

	private UUID id;

	private String name;

	private String genre;

	private String imageUrl;

	private LocalDate startDate;

	private LocalDate endDate;

	private String placeName;

	private String placeHall;

	private SearchPerformance(UUID id, String name, String genre, String imageUrl, LocalDate startDate,
		LocalDate endDate,
		String placeName, String placeHall) {
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.imageUrl = imageUrl;
		this.startDate = startDate;
		this.endDate = endDate;
		this.placeName = placeName;
		this.placeHall = placeHall;
	}

	public static SearchPerformance from(Performance performance) {
		return new SearchPerformance(performance.getId(), performance.getName(), performance.getGenre(),
			performance.getImageUrl(), performance.getStartDate(), performance.getEndDate(),
			performance.getPlace().getName(), performance.getPlace().getHall());
	}
}
