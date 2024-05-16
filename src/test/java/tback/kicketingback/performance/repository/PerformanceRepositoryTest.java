package tback.kicketingback.performance.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;

@SpringBootTest
class PerformanceRepositoryTest {

	@Autowired
	private PerformanceRepository performanceRepository;

	@Test
	@DisplayName("[정상] [장르 지정X] 공연 목록 검색")
	void getRankingPerformance() {
		int size = 20;
		Range range = DateUnit.WEEK.getRangeCalculator().apply(LocalDate.now());
		List<SimplePerformancePlaceDTO> rankingPerformances
			= performanceRepository.getRankingPerformances(range, GetPerformancesSize.of(size));

		assertThat(rankingPerformances).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(ints = {10, 20, 33, 45, 50})
	@DisplayName("[정상] [장르 지정X] 개수를 정해 공연 목록 검색")
	void getRankingPerformanceBySize(int size) {
		Range range = DateUnit.WEEK.getRangeCalculator().apply(LocalDate.now());
		List<SimplePerformancePlaceDTO> rankingPerformances = performanceRepository.getRankingPerformances(range,
			GetPerformancesSize.of(size));

		assertThat(rankingPerformances).isNotNull();
		assertThat(rankingPerformances.size()).isGreaterThanOrEqualTo(0);
		assertThat(rankingPerformances.size()).isLessThanOrEqualTo(size);
	}

	@ParameterizedTest
	@EnumSource(value = DateUnit.class)
	@DisplayName("[정상] [장르 지정X] (일간, 주간, 월간) 기간으로 공연 목록 검색")
	void getRankingPerformanceByDateUnit(DateUnit dateUnit) {
		int size = 10;
		Range range = dateUnit.getRangeCalculator().apply(LocalDate.now());
		List<SimplePerformancePlaceDTO> rankingPerformances
			= performanceRepository.getRankingPerformances(range, GetPerformancesSize.of(size));

		assertThat(rankingPerformances).isNotNull();
	}

	@Test
	@DisplayName("[정상] [장르 지정O] 공연 목록 검색")
	void getGenreRankingPerformance() {
		int size = 20;
		Range range = DateUnit.WEEK.getRangeCalculator().apply(LocalDate.now());

		List<SimplePerformancePlaceDTO> genreRankingPerformances = performanceRepository.getGenreRankingPerformances(
			Genre.MUSICAL, range, GetPerformancesSize.of(size));

		assertThat(genreRankingPerformances).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(ints = {10, 20, 33, 45, 50})
	@DisplayName("[정상] [장르 지정O] 개수를 정해 공연 목록 검색")
	void getRankingPerformanceByGenreAndSize(int size) {
		Range range = DateUnit.WEEK.getRangeCalculator().apply(LocalDate.now());

		List<SimplePerformancePlaceDTO> genreRankingPerformances = performanceRepository.getGenreRankingPerformances(
			Genre.THEATER, range, GetPerformancesSize.of(size));

		assertThat(genreRankingPerformances).isNotNull();
		assertThat(genreRankingPerformances.size()).isGreaterThanOrEqualTo(0);
		assertThat(genreRankingPerformances.size()).isLessThanOrEqualTo(size);
	}

	@ParameterizedTest
	@EnumSource(value = DateUnit.class)
	@DisplayName("[정상] [장르 지정O] (일간, 주간, 월간) 기간으로 공연 목록 검색")
	void getRankingPerformanceByGenreAndDateUnit(DateUnit dateUnit) {
		int size = 10;
		Range range = dateUnit.getRangeCalculator().apply(LocalDate.now());

		List<SimplePerformancePlaceDTO> genreRankingPerformances = performanceRepository.getGenreRankingPerformances(
			Genre.CLASSIC, range, GetPerformancesSize.of(size));

		assertThat(genreRankingPerformances).isNotNull();
	}

	@ParameterizedTest
	@EnumSource(value = Genre.class, names = {"NONE"}, mode = EnumSource.Mode.EXCLUDE)
	@DisplayName("[정상] [장르 지정O] 장르별로 공연 목록 검색")
	void getRankingPerformanceByGenre(Genre genre) {
		int size = 10;
		Range range = DateUnit.WEEK.getRangeCalculator().apply(LocalDate.now());
		List<SimplePerformancePlaceDTO> genreRankingPerformances = performanceRepository.getGenreRankingPerformances(
			genre, range, GetPerformancesSize.of(size));

		assertThat(genreRankingPerformances).isNotNull();
	}
}
