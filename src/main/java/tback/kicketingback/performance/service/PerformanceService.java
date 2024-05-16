package tback.kicketingback.performance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;
import tback.kicketingback.performance.repository.PerformanceRepository;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepository performanceRepository;

	public List<SimplePerformancePlaceDTO> getPerformances(
		String genre,
		GetPerformancesRequest getPerformancesRequest,
		LocalDate localDate
	) {
		Genre targetGenre = Genre.of(genre);
		DateUnit dateUnit = DateUnit.of(getPerformancesRequest.dateUnit());
		Range range = dateUnit.getRangeCalculator().apply(localDate);
		GetPerformancesSize getPerformancesSize = GetPerformancesSize.of(getPerformancesRequest.size());

		if (targetGenre.equals(Genre.NONE)) {
			return performanceRepository.getRankingPerformances(
				range,
				getPerformancesSize
			);
		}

		return performanceRepository.getGenreRankingPerformances(
			targetGenre,
			range,
			getPerformancesSize);
	}
}
