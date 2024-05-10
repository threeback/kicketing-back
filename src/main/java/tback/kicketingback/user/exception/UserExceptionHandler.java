package tback.kicketingback.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tback.kicketingback.global.exception.AbstractExceptionHandler;
import tback.kicketingback.user.exception.exceptions.*;

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
    public ResponseEntity<String> alreadyEmailAuthCompleteException(AlreadyEmailAuthCompleteException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }

    @ExceptionHandler(MismatchEmailAuthCodeException.class)
    public ResponseEntity<String> mismatchEmailAuthCodeException(MismatchEmailAuthCodeException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }

    @ExceptionHandler(PasswordEncodeException.class)
    public ResponseEntity<String> passwordEncodeException(PasswordEncodeException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }

    @ExceptionHandler(EmailAuthIncompleteException.class)
    public ResponseEntity<String> emailAuthIncompleteException(EmailAuthIncompleteException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<String> noSuchUserException(NoSuchUserException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }

    @ExceptionHandler(AuthInvalidStateException.class)
    public ResponseEntity<String> authInvalidStateException(AuthInvalidStateException exception) {
        return getBadRequestResponseEntity(exception.getMessage());
    }
}
