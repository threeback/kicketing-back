package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class AuthenticationFailException extends RuntimeException{

	public AuthenticationFailException(JwtTokenType tokenType) {
		super(tokenType.name() + "토큰 추출 실패");
	}
}
