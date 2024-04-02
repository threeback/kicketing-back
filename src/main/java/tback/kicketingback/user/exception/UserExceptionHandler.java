package tback.kicketingback.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tback.kicketingback.user.exception.exceptions.*;

@RestControllerAdvice
public class UserExceptionHandler {

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

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<String> emailDuplicatedException(EmailDuplicatedException exception) {
        return getForbiddenResponseEntity(exception.getMessage());
    }

    private ResponseEntity<String> getNotFoundResponseEntity(final String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    private ResponseEntity<String> getBadRequestResponseEntity(final String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    private ResponseEntity<String> getForbiddenResponseEntity(final String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(message);
    }
}
