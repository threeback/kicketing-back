package tback.kicketingback.performance.domain.type;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tback.kicketingback.performance.exception.exceptions.InvalidGenreException;

class GenreTest {

	@ParameterizedTest
	@ValueSource(strings = {"NONE", "CONCERT", "MUSICAL", "CLASSIC", "THEATER"})
	@DisplayName("[정상] 이름으로 장르 객체 획득")
	void getGenreByName(String genreName) {
		assertThatCode(() -> {
			Genre.of(genreName);
		}).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@ValueSource(strings = {"none", "CONCert", "Musical", "CLASSIC", "tHEATER"})
	@DisplayName("[정상] 이름은 대소문자를 구별하지 않음")
	void getGenreByNameIgnoreCase(String genreName) {
		assertThatCode(() -> {
			Genre.of(genreName);
		}).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@ValueSource(strings = {"music", "iceCream", "!@#EW!@", "12we21"})
	@DisplayName("[예외] 정의되지 않은 이름은 예외를 던짐")
	void getGenreByWrongName(String wrongGenreName) {
		assertThatThrownBy(() -> {
			Genre.of(wrongGenreName);
		}).isInstanceOf(InvalidGenreException.class);
	}
}
