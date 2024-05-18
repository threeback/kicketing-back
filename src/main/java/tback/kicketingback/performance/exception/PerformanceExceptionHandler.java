package tback.kicketingback.performance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tback.kicketingback.global.exception.AbstractExceptionHandler;
import tback.kicketingback.performance.dto.AlreadySelectedSeatResponse;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.exception.exceptions.DuplicateSeatSelectionException;
import tback.kicketingback.performance.exception.exceptions.InvalidGenreException;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceDateUnitException;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceSizeException;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceUUIDException;

@RestControllerAdvice
public class PerformanceExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(InvalidGenreException.class)
	public ResponseEntity<String> invalidGenreException(InvalidGenreException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidGetPerformanceDateUnitException.class)
	public ResponseEntity<String> invalidGetPerformanceDateUnitException(
		InvalidGetPerformanceDateUnitException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidGetPerformanceSizeException.class)
	public ResponseEntity<String> invalidGetPerformanceSizeException(InvalidGetPerformanceSizeException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidPerformanceUUIDException.class)
	public ResponseEntity<String> invalidPerformanceUUIDException(InvalidPerformanceUUIDException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(DuplicateSeatSelectionException.class)
	public ResponseEntity<String> duplicateSeatSelectionException(DuplicateSeatSelectionException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(AlreadySelectedSeatException.class)
	public ResponseEntity<AlreadySelectedSeatResponse> alreadySelectedSeatException(
		AlreadySelectedSeatException exception) {
		return getBadRequestResponseEntity(exception,
			new AlreadySelectedSeatResponse(exception.getMessage(), exception.getSeatRowCol()));
	}
}
