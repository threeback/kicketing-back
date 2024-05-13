package tback.kicketingback.performance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.Performance;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.repository.PerformanceRepository;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepository performanceRepository;

	public List<Performance> getPerformances(String genre, GetPerformancesRequest getPerformancesRequest) {
		Genre targetGenre = Genre.of(genre);
		DateUnit dateUnit = DateUnit.of(getPerformancesRequest.dateUnit());
		GetPerformancesSize getPerformancesSize = GetPerformancesSize.of(getPerformancesRequest.size());

		if (targetGenre.equals(Genre.NONE)) {
			return performanceRepository.getRankingPerformances(
				dateUnit,
				getPerformancesSize
			);
		}

		return performanceRepository.getGenreRankingPerformances(
			targetGenre,
			dateUnit,
			getPerformancesSize);
	}
}
