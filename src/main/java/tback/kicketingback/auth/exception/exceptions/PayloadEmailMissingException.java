package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class PayloadEmailMissingException extends RuntimeException {

	public PayloadEmailMissingException(JwtTokenType tokenType) {
		super("[%s] JWT 페이로드에 이메일 없음".formatted(tokenType.name()));
	}
}
