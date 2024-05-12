package tback.kicketingback.performance.domain.type;

import lombok.Getter;
import tback.kicketingback.performance.exception.exceptions.InvalidGenreException;

@Getter
public enum Genre {

	NONE("없음"), CONCERT("콘서트"), MUSICAL("뮤지컬"), CLASSIC("클래식"), THEATER("연극");

	private final String value;

	Genre(String value) {
		this.value = value;
	}

	public static Genre of(String name) {
		Genre genre;

		try {
			genre = Genre.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new InvalidGenreException(name);
		}

		return genre;
	}
}
