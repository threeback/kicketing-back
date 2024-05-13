package tback.kicketingback.performance.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tback.kicketingback.performance.domain.Performance;

@Getter
@NoArgsConstructor
public class PerformanceDTO {

	private UUID id;
	private String name;
	private String genre;
	private int length;
	private LocalDate startDate;
	private LocalDate endDate;
	private int ageLimit;
	private String imageUrl;
	private long placeId;

	private PerformanceDTO(UUID id, String name, String genre, int length, LocalDate startDate, LocalDate endDate,
		int ageLimit, String imageUrl, long placeId) {
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.length = length;
		this.startDate = startDate;
		this.endDate = endDate;
		this.ageLimit = ageLimit;
		this.imageUrl = imageUrl;
		this.placeId = placeId;
	}

	public static PerformanceDTO of(Performance performance) {
		return new PerformanceDTO(
			performance.getId(),
			performance.getName(),
			performance.getGenre(),
			performance.getLength(),
			performance.getStartDate(),
			performance.getEndDate(),
			performance.getAgeLimit(),
			performance.getImageUrl(),
			performance.getPlace().getId()
		);
	}
}
