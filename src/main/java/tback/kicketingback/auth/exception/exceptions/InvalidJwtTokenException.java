package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class InvalidJwtTokenException extends RuntimeException {

	public InvalidJwtTokenException(JwtTokenType tokenType) {
		super(tokenType.name() + "JWT 유효성 검증 실패");
	}
}
