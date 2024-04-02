package tback.kicketingback.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractExceptionHandler {
	protected final ResponseEntity<String> getNotFoundResponseEntity(final String message) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(message);
	}

	protected final ResponseEntity<String> getBadRequestResponseEntity(final String message) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(message);
	}

	protected final ResponseEntity<String> getForbiddenResponseEntity(final String message) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(message);
	}
}
