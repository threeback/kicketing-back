package tback.kicketingback.global.exception;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		return getBadRequestResponseEntity(exception,
			Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> ConstraintViolationException(ConstraintViolationException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}
}
