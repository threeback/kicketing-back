package tback.kicketingback.performance.dto;

import java.util.List;

public record GetPerformancesResponse(
	List<SimplePerformancePlaceDTO> performances
) {
}
