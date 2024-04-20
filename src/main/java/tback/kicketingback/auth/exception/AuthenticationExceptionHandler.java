package tback.kicketingback.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tback.kicketingback.auth.exception.exceptions.TokenExtractionException;
import tback.kicketingback.auth.exception.exceptions.ExpiredTokenException;
import tback.kicketingback.auth.exception.exceptions.InvalidJwtTokenException;
import tback.kicketingback.auth.exception.exceptions.PayloadEmailMissingException;
import tback.kicketingback.global.exception.AbstractExceptionHandler;

@RestControllerAdvice
public class AuthenticationExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(TokenExtractionException.class)
	public ResponseEntity<String> TokenExtractionException(TokenExtractionException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(ExpiredTokenException.class)
	public ResponseEntity<String> ExpiredTokenException(ExpiredTokenException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<String> InvalidJwtTokenException(InvalidJwtTokenException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(PayloadEmailMissingException.class)
	public ResponseEntity<String> PayloadEmailMissingException(PayloadEmailMissingException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}
}
