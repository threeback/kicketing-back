package tback.kicketingback.performance.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Function;

import lombok.Getter;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceDateUnitException;

@Getter
public enum DateUnit {
	DAY((localDate) -> new Range(LocalDate.now(), LocalDate.now().plusDays(1))),
	WEEK((localDate) -> {
		LocalDate firstDayOfWeek = localDate.with(DayOfWeek.MONDAY);
		LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(7);

		return new Range(firstDayOfWeek, lastDayOfWeek);
	}),
	MONTH((localDate) -> {
		LocalDate firstDayOfMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

		return new Range(firstDayOfMonth, lastDayOfMonth);
	});

	private final Function<LocalDate, Range> rangeCalculator;

	DateUnit(Function<LocalDate, Range> rangeCalculator) {
		this.rangeCalculator = rangeCalculator;
	}

	public static DateUnit of(String unitType) {
		DateUnit dateUnit;

		try {
			dateUnit = DateUnit.valueOf(unitType.toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new InvalidGetPerformanceDateUnitException(unitType);
		}

		return dateUnit;
	}
}
