package tback.kicketingback.performance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import tback.kicketingback.global.exception.AbstractExceptionHandler;
import tback.kicketingback.performance.dto.AlreadySelectedSeatResponse;
import tback.kicketingback.performance.exception.exceptions.AlreadySelectedSeatException;
import tback.kicketingback.performance.exception.exceptions.DuplicateSeatSelectionException;
import tback.kicketingback.performance.exception.exceptions.InvalidGenreException;
import tback.kicketingback.performance.exception.exceptions.InvalidGetDiscountTypeException;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceDateUnitException;
import tback.kicketingback.performance.exception.exceptions.InvalidGetPerformanceSizeException;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;
import tback.kicketingback.performance.exception.exceptions.InvalidPayCancelRequestException;
import tback.kicketingback.performance.exception.exceptions.InvalidPayRequestException;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceException;
import tback.kicketingback.performance.exception.exceptions.InvalidPerformanceUUIDException;
import tback.kicketingback.performance.exception.exceptions.InvalidReservationDataException;
import tback.kicketingback.performance.exception.exceptions.InvalidSeatIdException;
import tback.kicketingback.performance.exception.exceptions.NoAvailableSeatsException;
import tback.kicketingback.performance.exception.exceptions.NoSuchReservationException;
import tback.kicketingback.performance.exception.exceptions.PaymentCancelServerErrorException;
import tback.kicketingback.performance.exception.exceptions.PaymentServerErrorException;
import tback.kicketingback.performance.exception.exceptions.UnableCancelException;

@RestControllerAdvice
@Slf4j
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

	@ExceptionHandler(InvalidOnStageIDException.class)
	public ResponseEntity<String> invalidOnStageIDException(InvalidOnStageIDException exception) {
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

	@ExceptionHandler(InvalidPerformanceException.class)
	public ResponseEntity<String> invalidPerformanceException(InvalidPerformanceException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(NoAvailableSeatsException.class)
	public ResponseEntity<String> noAvailableSeatsException(NoAvailableSeatsException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidSeatIdException.class)
	public ResponseEntity<String> invalidSeatIdException(InvalidSeatIdException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(NoSuchReservationException.class)
	public ResponseEntity<String> noSuchReservationException(NoSuchReservationException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidGetDiscountTypeException.class)
	public ResponseEntity<String> invalidGetDiscountTypeException(InvalidGetDiscountTypeException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidPayRequestException.class)
	public ResponseEntity<String> invalidPayRequestException(InvalidPayRequestException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(PaymentServerErrorException.class)
	public ResponseEntity<String> paymentServerErrorException(PaymentServerErrorException exception) {
		return getBadGatewayResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidReservationDataException.class)
	public ResponseEntity<String> invalidReservationDataException(InvalidReservationDataException exception) {
		log.error("DB 무결성 깨짐 {}", exception.getReservations());
		return getServerErrorResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(UnableCancelException.class)
	public ResponseEntity<String> unableCancelException(UnableCancelException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(InvalidPayCancelRequestException.class)
	public ResponseEntity<String> invalidPayCancelRequestException(InvalidPayCancelRequestException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}

	@ExceptionHandler(PaymentCancelServerErrorException.class)
	public ResponseEntity<String> paymentCancelServerErrorException(PaymentCancelServerErrorException exception) {
		return getBadGatewayResponseEntity(exception, exception.getMessage());
	}
}
