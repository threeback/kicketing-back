package tback.kicketingback.auth.oauth.domain;

import tback.kicketingback.auth.oauth.dto.OauthUser;

public interface OauthClient {

	OauthUser getOauthUser(String authCode);
}
