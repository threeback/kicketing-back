package tback.kicketingback.performance.dto;

import java.util.List;

import lombok.Getter;
import tback.kicketingback.performance.domain.PerformanceAutoComplete;

@Getter
public class PerformanceAutoCompleteResponse {
	private final List<PerformanceAutoComplete> performances;

	private PerformanceAutoCompleteResponse(List<PerformanceAutoComplete> performances) {
		this.performances = performances;
	}

	public static PerformanceAutoCompleteResponse from(List<PerformanceAutoComplete> performances) {
		return new PerformanceAutoCompleteResponse(performances);
	}
}
