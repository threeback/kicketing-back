package tback.kicketingback.performance.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tback.kicketingback.performance.domain.type.Genre;

public record PerformanceSearchRequest(
	@Size(min = 2, message = "검색어는 2글자 이상이어야 합니다.")
	String name,

	@NotNull(message = "잘못된 검색 조건입니다.")
	List<Genre> genres
) {

}
