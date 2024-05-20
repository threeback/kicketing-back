package tback.kicketingback.performance.exception.exceptions;

public class InvalidOnStageIDException extends RuntimeException {
	public InvalidOnStageIDException(Long onStageID) {
		super("%s: 유효하지 않은 무대 ID".formatted(onStageID));
	}
}
