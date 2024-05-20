package tback.kicketingback.performance.service;

import java.time.LocalDate;
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
		LocalDate startDate = getBookableDatesRequest.startDate();
		Range range = calculateRange(startDate);

		List<OnStage> onStages = onStageRepository.findByPerformance_IdAndDateTimeBetween(
			performanceUUID,
			startDate.atStartOfDay(),
			range.end().atStartOfDay()
		);

		if (onStages.isEmpty() && performanceNotOnStageExists(performanceUUID)) {
			throw new InvalidPerformanceUUIDException(performanceUUID);
		}

		return onStages.stream()
			.map(SimpleOnStageDTO::from)
			.toList();
	}

	private Range calculateRange(LocalDate startDate) {
		return DateUnit.of("month").getRangeCalculator().apply(startDate);
	}

	private boolean performanceNotOnStageExists(UUID performanceUUID) {
		return !onStageRepository.existsByPerformance_Id(performanceUUID);
	}
}
