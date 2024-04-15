package tback.kicketingback.auth.oauth.exception.exceptions;

public class UnsupportedOAuthDomainException extends RuntimeException {

	public UnsupportedOAuthDomainException(String unsupportedDomain) {
		super("[%s] 지원하지 않는 도메인입니다.".formatted(unsupportedDomain));
	}
}
