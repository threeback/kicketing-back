package tback.kicketingback.performance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetPerformancesRequest(
	@NotNull(message = "유효하지 않은 검색 기간 [day|week|month] 필")
	String dateUnit,

	@NotNull(message = "유효하지 않은 검색 개수: null [10 ~ 50의 양수] 필요")
	@Positive(message = "유효하지 않은 검색 개수: [10 ~ 50의 양수] 필요")
	Integer size
) {

}
