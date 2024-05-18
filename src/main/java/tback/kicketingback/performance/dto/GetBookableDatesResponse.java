package tback.kicketingback.performance.dto;

import java.util.List;

public record GetBookableDatesResponse(
	List<OnStageDTO> bookableDates
) {
}
