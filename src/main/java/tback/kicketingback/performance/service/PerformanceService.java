package tback.kicketingback.performance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.DetailPerformanceDTO;
import tback.kicketingback.performance.dto.GetPerformancesRequest;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.PerformancePlaceDTO;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SeatGradeDTO;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;
import tback.kicketingback.performance.dto.StarDTO;
import tback.kicketingback.performance.repository.PerformanceRepository;
import tback.kicketingback.performance.repository.SeatGradeRepository;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepository performanceRepository;
	private final SeatGradeRepository seatGradeRepository;

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
			return performanceRepository.findRankingPerformances(
				range,
				getPerformancesSize
			);
		}

		return performanceRepository.findGenreRankingPerformances(
			targetGenre,
			range,
			getPerformancesSize);
	}

	public DetailPerformanceDTO getPerformance(UUID performanceUUID) {
		PerformancePlaceDTO performancePlaceDTO = performanceRepository.findPerformanceAndPlaceInfo(performanceUUID);
		List<SeatGradeDTO> seatGradeDTOS = seatGradeRepository.findSeatGradesByPerformanceId(performanceUUID).stream()
			.map(seatGrade ->
				new SeatGradeDTO(seatGrade.getId(),
					seatGrade.getPerformance().getId(),
					seatGrade.getGrade(),
					seatGrade.getPrice()))
			.toList();
		List<StarDTO> starsDTOS = performanceRepository.findStarsIn(performanceUUID);

		return new DetailPerformanceDTO(
			performancePlaceDTO.performanceDTO(),
			performancePlaceDTO.placeDTO(),
			seatGradeDTOS, starsDTOS);
	}
}
