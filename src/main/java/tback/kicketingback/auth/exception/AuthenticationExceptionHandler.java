package tback.kicketingback.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import tback.kicketingback.global.AbstractExceptionHandler;
import tback.kicketingback.auth.exception.exceptions.InvalidJwtTokenException;
import tback.kicketingback.auth.exception.exceptions.PayloadEmailMissingException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;

@RestControllerAdvice
public class AuthenticationExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(AuthInvalidEmailException.class)
	public ResponseEntity<String> PayloadEmailMissingException(PayloadEmailMissingException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> InvalidJwtTokenException(InvalidJwtTokenException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}
}
