package tback.kicketingback.auth.oauth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;
import tback.kicketingback.global.AbstractExceptionHandler;

@RestControllerAdvice
public class OauthExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(OAuthResourceAccessFailureException.class)
	public ResponseEntity<String> OAuthResourceAccessFailureException(OAuthResourceAccessFailureException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

}
