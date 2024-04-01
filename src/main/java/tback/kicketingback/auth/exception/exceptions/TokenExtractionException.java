package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class TokenExtractionException extends RuntimeException{

	public TokenExtractionException(JwtTokenType tokenType) {
		super(tokenType.name() + "토큰 추출 실패");
	}
}
