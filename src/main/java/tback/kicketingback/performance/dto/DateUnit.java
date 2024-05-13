package tback.kicketingback.performance.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Supplier;

import lombok.Getter;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceDateUnitException;

@Getter
public enum DateUnit {
	DAY(() -> new Range(LocalDate.now(), LocalDate.now().plusDays(1))),
	WEEK(() -> {
		LocalDate today = LocalDate.now();

		LocalDate firstDayOfWeek = today.with(DayOfWeek.MONDAY);
		LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(7);

		return new Range(firstDayOfWeek, lastDayOfWeek);
	}),
	MONTH(() -> {
		LocalDate today = LocalDate.now();

		LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

		return new Range(firstDayOfMonth, lastDayOfMonth);
	});

	private final Supplier<Range> rangeSupplier;

	DateUnit(Supplier<Range> rangeSupplier) {
		this.rangeSupplier = rangeSupplier;
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
