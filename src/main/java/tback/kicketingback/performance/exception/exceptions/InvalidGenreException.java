package tback.kicketingback.performance.exception.exceptions;

public class InvalidGenreException extends RuntimeException {
	public InvalidGenreException(String name) {
		super("%s: 유효하지 않은 장르".formatted(name));
	}
}
