package tback.kicketingback.performance.dto;

import jakarta.validation.constraints.NotNull;

public record GetPerformancesRequest(
	@NotNull(message = "검색할 공연의 기간 범위가 없음")
	String dateUnit,

	@NotNull(message = "검색할 공연의 개수가 없음")
	Integer size
) {

}
