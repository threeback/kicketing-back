package tback.kicketingback.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractExceptionHandler {
	protected final ResponseEntity<String> getNotFoundResponseEntity(final Exception exception, final String message) {
		log.info("{}: {} response send because of NOT_FOUND: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	protected final ResponseEntity<String> getBadRequestResponseEntity(final Exception exception,
		final String message) {
		log.info("{}: {} response send because of Bad Request: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	protected final <T> ResponseEntity<T> getBadRequestResponseEntity(final Exception exception, final T info) {
		log.info("{}: {} response send because of Bad Request: {}", this.getClass(), exception.getClass(), info);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(info);
	}

	protected final ResponseEntity<String> getForbiddenResponseEntity(final Exception exception, final String message) {
		log.info("{}: {} response send because of Forbidden: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
	}

	protected final ResponseEntity<String> getConflictResponseEntity(final Exception exception, final String message) {
		log.info("{}: {} response send because of Conflict: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}

	protected final ResponseEntity<String> getServerErrorResponseEntity(
		final Exception exception,
		final String message
	) {
		log.error("{}: {} response send because of ServerError: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
	}

	protected final ResponseEntity<String> getBadGatewayResponseEntity(final Exception exception, final String message) {
		log.info("{}: {} response send because of Bad Gateway: {}", this.getClass(), exception.getClass(), message);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
	}
}
