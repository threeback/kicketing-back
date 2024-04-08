package tback.kicketingback.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tback.kicketingback.global.AbstractExceptionHandler;
import tback.kicketingback.user.exception.exceptions.AlreadyEmailAuthCompleteException;
import tback.kicketingback.user.exception.exceptions.AlreadySamePasswordException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidNameException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailCreateException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.exception.exceptions.EmailSendException;
import tback.kicketingback.user.exception.exceptions.MismatchEmailAuthCodeException;
import tback.kicketingback.user.exception.exceptions.UserPasswordEmptyException;

@RestControllerAdvice
public class UserExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(AuthInvalidEmailException.class)
	public ResponseEntity<String> authInvalidEmailException(AuthInvalidEmailException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(AuthInvalidPasswordException.class)
	public ResponseEntity<String> authInvalidPasswordException(AuthInvalidPasswordException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(EmailFormatException.class)
	public ResponseEntity<String> emailNotFoundException(EmailFormatException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(UserPasswordEmptyException.class)
	public ResponseEntity<String> memberPasswordEmptyException(UserPasswordEmptyException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(AlreadySamePasswordException.class)
	public ResponseEntity<String> alreadySamePasswordException(AlreadySamePasswordException exception) {
		return getForbiddenResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(EmailDuplicatedException.class)
	public ResponseEntity<String> emailDuplicatedException(EmailDuplicatedException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(AuthInvalidNameException.class)
	public ResponseEntity<String> authInvalidNameException(AuthInvalidNameException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(EmailCreateException.class)
	public ResponseEntity<String> emailCreateException(EmailCreateException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(EmailSendException.class)
	public ResponseEntity<String> emailSendException(EmailSendException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(AlreadyEmailAuthCompleteException.class)
	public ResponseEntity<String> AlreadyEmailAuthCompleteException(AlreadyEmailAuthCompleteException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}

	@ExceptionHandler(MismatchEmailAuthCodeException.class)
	public ResponseEntity<String> MismatchEmailAuthCodeException(MismatchEmailAuthCodeException exception) {
		return getBadRequestResponseEntity(exception.getMessage());
	}
}
