package tback.kicketingback.global.exception;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
		return getBadRequestResponseEntity(exception,
			Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		return getBadRequestResponseEntity(exception, exception.getMessage());
	}
}
