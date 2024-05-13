package tback.kicketingback.performance.dto;

import java.util.List;

import tback.kicketingback.performance.domain.Performance;

public record GetPerformancesResponse(
	List<Performance> performances
) {
}
