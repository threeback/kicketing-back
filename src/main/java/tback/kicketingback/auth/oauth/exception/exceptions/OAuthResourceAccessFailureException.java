package tback.kicketingback.auth.oauth.exception.exceptions;

public class OAuthResourceAccessFailureException extends RuntimeException {

	public OAuthResourceAccessFailureException() {
		super("Oauth Resource 가져오기 실패");
	}
}
