package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class ExpiredTokenException extends RuntimeException {

	public ExpiredTokenException(JwtTokenType tokenType) {
		super(tokenType.name() + "JWT 토큰 만료");
	}
}
