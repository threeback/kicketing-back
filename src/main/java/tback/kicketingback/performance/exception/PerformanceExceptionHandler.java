package tback.kicketingback.performance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tback.kicketingback.global.exception.AbstractExceptionHandler;
import tback.kicketingback.performance.exception.exceptions.InvalidGenreException;

@RestControllerAdvice
public class PerformanceExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(InvalidGenreException.class)
	public ResponseEntity<String> invalidGenreException(InvalidGenreException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}
}
