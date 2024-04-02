package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class ExpiredTokenException extends RuntimeException{

	public ExpiredTokenException(JwtTokenType tokenType) {
		super(tokenType.name() + "JWT 만료된 토큰입니다.");
	}
}
