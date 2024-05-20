package tback.kicketingback.performance.dto;

import java.time.LocalDateTime;

import tback.kicketingback.performance.domain.OnStage;

public record SimpleOnStageDTO(
	Long id,
	LocalDateTime dateTime,
	int round
) {
	public static SimpleOnStageDTO from(OnStage onStage) {
		return new SimpleOnStageDTO(
			onStage.getId(),
			onStage.getDateTime(),
			onStage.getRound()
		);
	}
}