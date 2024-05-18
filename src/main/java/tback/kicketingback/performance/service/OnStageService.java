package tback.kicketingback.performance.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.OnStage;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.GetBookableDatesRequest;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SimpleOnStageDTO;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceUUIDException;
import tback.kicketingback.performance.repository.OnStageRepository;

@Service
@RequiredArgsConstructor
public class OnStageService {

	private final OnStageRepository onStageRepository;

	public List<SimpleOnStageDTO> getBookableDates(UUID performanceUUID,
		GetBookableDatesRequest getBookableDatesRequest) {
		Range range = DateUnit.of("month").getRangeCalculator().apply(getBookableDatesRequest.startDate());

		List<OnStage> onStages = onStageRepository.findByPerformance_IdAndDateTimeBetween(
			performanceUUID,
			range.start().atStartOfDay(),
			range.end().atStartOfDay()
		);

		if (onStages.isEmpty()) {
			throw new InvalidPerformanceUUIDException(performanceUUID);
		}

		return onStages.stream()
			.map(this::convertToSimpleOnStageDTO)
			.toList();
	}

	private SimpleOnStageDTO convertToSimpleOnStageDTO(OnStage onStage) {
		return new SimpleOnStageDTO(
			onStage.getId(),
			onStage.getDateTime(),
			onStage.getRound()
		);
	}
}
