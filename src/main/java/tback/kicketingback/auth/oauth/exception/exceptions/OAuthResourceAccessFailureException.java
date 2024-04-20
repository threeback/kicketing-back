package tback.kicketingback.auth.oauth.exception.exceptions;

import tback.kicketingback.auth.oauth.domain.OauthClient;

public class OAuthResourceAccessFailureException extends RuntimeException {

	public OAuthResourceAccessFailureException(OauthClient oauthClient, String accessToken) {
		super("[%s] [%s]:  Oauth Resource 가져오기 실패".formatted(oauthClient.getClass(), accessToken));
	}
}
