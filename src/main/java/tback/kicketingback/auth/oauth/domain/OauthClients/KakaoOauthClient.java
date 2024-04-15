package tback.kicketingback.auth.oauth.domain.OauthClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.dto.KakaoUserInfo;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.dto.ResponseKakaoToken;
import tback.kicketingback.auth.oauth.dto.ResponseKakaoUser;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;

@Component
@RequiredArgsConstructor
public class KakaoOauthClient implements OauthClient {

	private static final String GET_TOKEN = "authorization_code";

	private final RestTemplate restTemplate;

	@Value("${kakao.client_id}")
	String clientId;

	@Value("${kakao.token.redirect.uri}")
	String redirectUri;

	@Value("${kakao.get.user.token.uri}")
	String getTokenApiUrl;

	@Value("${kakao.get.user.url}")
	String getUserApiUrl;

	@Override
	public OauthUser getOauthUser(String authCode, String state) {
		ResponseKakaoToken token = getToken(authCode, state);
		return getUser(token.accessToken());
	}

	private ResponseKakaoToken getToken(String authCode, String state) {
		HttpEntity<String> requestEntity = getTokenRequestEntity();
		String requestTokenURI = tokenRequestURI(authCode, state);
		ResponseEntity<ResponseKakaoToken> response =
			restTemplate.exchange(requestTokenURI, HttpMethod.GET, requestEntity, ResponseKakaoToken.class);
		return response.getBody();
	}

	private HttpEntity<String> getTokenRequestEntity() {
		HttpHeaders httpHeaders = new HttpHeaders();
		addHeaderContentType(httpHeaders);
		HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
		return requestEntity;
	}

	private String tokenRequestURI(String code, String state) {
		return UriComponentsBuilder.fromHttpUrl(getTokenApiUrl)
			.queryParam("grant_type", GET_TOKEN)
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("code", code)
			.queryParam("state", state)
			.toUriString();
	}

	private OauthUser getUser(String accessToken) {
		HttpEntity<String> requestEntity = userRequestURL(accessToken);

		ResponseEntity<ResponseKakaoUser> responseEntity =
			restTemplate.exchange(getUserApiUrl, HttpMethod.GET, requestEntity, ResponseKakaoUser.class);

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new OAuthResourceAccessFailureException(this, accessToken);
		}

		ResponseKakaoUser body = responseEntity.getBody();
		KakaoUserInfo kakaoUserInfo = body.kakaoUserInfo();
		return new OauthUser(kakaoUserInfo.kakaoProfile().nickname(), kakaoUserInfo.email());
	}

	private HttpEntity<String> userRequestURL(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		addHeaderContentType(httpHeaders);
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		return new HttpEntity<>(httpHeaders);
	}

	private void addHeaderContentType(HttpHeaders httpHeaders) {
		httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	}
}
