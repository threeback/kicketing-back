package tback.kicketingback.performance.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceSizeException;

class GetPerformancesSizeTest {

	@ParameterizedTest
	@ValueSource(ints = {10, 11, 14, 20, 40, 45, 50})
	@DisplayName("[정상] 검색할 공연 목록의 개수를 10 ~ 50개로 정함")
	void getSize(int size) {
		assertThatCode(() -> {
			GetPerformancesSize.of(size);
		}).doesNotThrowAnyException();

		GetPerformancesSize getPerformancesSize = GetPerformancesSize.of(size);
		int anIntSize = getPerformancesSize.getAnInt();
		assertThat(size).isSameAs(anIntSize);
	}

	@ParameterizedTest
	@ValueSource(ints = {-123, -23, -2, -1, 0, 1, 9, 51, 111})
	@DisplayName("[예외] 검색 범위를 벗어난 숫자는 예외를 던짐")
	void getOutOfRangeSize(int outOfRange) {
		assertThatThrownBy(() -> {
			GetPerformancesSize.of(outOfRange);
		}).isInstanceOf(InvalidGetPerformanceSizeException.class);
	}

}
