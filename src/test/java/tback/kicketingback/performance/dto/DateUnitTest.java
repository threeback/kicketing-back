package tback.kicketingback.performance.dto;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceDateUnitException;

class DateUnitTest {

	@ParameterizedTest
	@ValueSource(strings = {"DAY", "WEEK", "MONTH"})
	@DisplayName("[정상] 날짜 단위로 날짜 단위 객체를 획득")
	void getDateUnitByName(String dateUnitName) {
		assertThatCode(() -> {
			DateUnit.of(dateUnitName);
		}).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@ValueSource(strings = {"day", "Week", "MonTH"})
	@DisplayName("[정상] 대소문자를 구별하지 않음")
	void getDateUnitByNameIgnoreCase(String dateUnitName) {
		assertThatCode(() -> {
			DateUnit.of(dateUnitName);
		}).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@ValueSource(strings = {"year", "decade", "ahffn"})
	@DisplayName("[예외] 정의되지 않은 날짜 단위는 예외를 던짐")
	void getDateUnitByWrongName(String wrongDateUnitName) {
		assertThatThrownBy(() -> {
			DateUnit.of(wrongDateUnitName);
		}).isInstanceOf(InvalidGetPerformanceDateUnitException.class);
	}

	@Test
	@DisplayName("[정상] 일간 검색 범위를 결정함")
	void calculateDailyRange() {
		DateUnit day = DateUnit.DAY;
		Function<LocalDate, Range> rangeCalculator = day.getRangeCalculator();
		Range range = rangeCalculator.apply(LocalDate.now());

		long days = Duration.between(range.start().atStartOfDay(), range.end().atStartOfDay()).toDays();

		assertThat(days).isSameAs(1L);
	}

	@Test
	@DisplayName("[정상] 주간 검색 범위를 결정함")
	void calculateWeeklyRange() {
		DateUnit day = DateUnit.WEEK;
		Function<LocalDate, Range> rangeCalculator = day.getRangeCalculator();
		Range range = rangeCalculator.apply(LocalDate.now());

		long days = Duration.between(range.start().atStartOfDay(), range.end().atStartOfDay()).toDays();

		assertThat(days).isSameAs(7L);
	}

	@Test
	@DisplayName("[정상] 월간 검색 범위를 결정함")
	void calculateMonthlyRange() {
		DateUnit day = DateUnit.MONTH;
		Function<LocalDate, Range> rangeCalculator = day.getRangeCalculator();
		Range range = rangeCalculator.apply(LocalDate.now());

		long days = Duration.between(range.start().atStartOfDay(), range.end().atStartOfDay()).toDays();

		assertThat(days).isGreaterThanOrEqualTo(28L);
		assertThat(days).isLessThanOrEqualTo(31L);
	}
}
