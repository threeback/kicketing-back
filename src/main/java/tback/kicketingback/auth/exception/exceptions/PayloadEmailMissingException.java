package tback.kicketingback.auth.exception.exceptions;

import tback.kicketingback.auth.jwt.JwtTokenType;

public class PayloadEmailMissingException extends RuntimeException {

	public PayloadEmailMissingException(JwtTokenType tokenType) {
		super(tokenType.name() + "JWT 페이로드에 이메일이 없습니다.");
	}
}
