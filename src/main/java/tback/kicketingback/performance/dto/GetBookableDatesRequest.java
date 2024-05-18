package tback.kicketingback.performance.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public record GetBookableDatesRequest(
	@NotNull(message = "공연 예매 가능 날짜를 조회할 시작 날짜 정보가 없음")
	@DateTimeFormat(pattern = "yyyyMMdd")
	LocalDate startDate
) {
}
