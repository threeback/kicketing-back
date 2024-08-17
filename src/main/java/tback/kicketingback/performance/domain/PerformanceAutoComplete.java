package tback.kicketingback.performance.domain;

import java.util.UUID;

import lombok.Getter;

@Getter
public class PerformanceAutoComplete {

	private UUID id;

	private String name;

	private String genre;

	private PerformanceAutoComplete(UUID id, String name, String genre) {
		this.id = id;
		this.name = name;
		this.genre = genre;
	}

	public static PerformanceAutoComplete from(Performance performance) {
		return new PerformanceAutoComplete(performance.getId(), performance.getName(),
			performance.getGenre());
	}
}
