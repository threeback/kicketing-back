package tback.kicketingback.performance.dto;

import java.util.List;

import lombok.Getter;
import tback.kicketingback.performance.domain.SearchPerformance;

@Getter
public class PerformanceSearchResponse {

	private final List<SearchPerformance> performances;

	private PerformanceSearchResponse(List<SearchPerformance> performances) {
		this.performances = performances;
	}

	public static PerformanceSearchResponse from(List<SearchPerformance> performances) {
		return new PerformanceSearchResponse(performances);
	}
}
