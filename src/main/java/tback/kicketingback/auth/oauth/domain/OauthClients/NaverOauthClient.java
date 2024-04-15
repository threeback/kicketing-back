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
import tback.kicketingback.auth.oauth.dto.NaverUserInfo;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.dto.ResponseNaverToken;
import tback.kicketingback.auth.oauth.dto.ResponseNaverUser;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;

@Component
@RequiredArgsConstructor
public class NaverOauthClient implements OauthClient {

	private static final String GET_TOKEN = "authorization_code";

	private final RestTemplate restTemplate;

	@Value("${naver.client_id}")
	String clientId;

	@Value("${naver.client_secret}")
	String clientSecret;

	@Value("${naver.get.user.token.uri}")
	String getTokenApiUrl;

	@Value("${naver.get.user.url}")
	String getUserApiUrl;

	@Override
	public OauthUser getOauthUser(String authCode, String state) {
		ResponseNaverToken token = getToken(authCode, state);
		return getUser(token.accessToken());
	}

	private ResponseNaverToken getToken(String authCode, String state) {
		String requestTokenURI = tokenRequestURI(authCode, state);
		ResponseEntity<ResponseNaverToken> response =
			restTemplate.exchange(requestTokenURI, HttpMethod.GET, null, ResponseNaverToken.class);
		return response.getBody();
	}

	private String tokenRequestURI(String code, String state) {
		return UriComponentsBuilder.fromHttpUrl(getTokenApiUrl)
			.queryParam("grant_type", GET_TOKEN)
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret)
			.queryParam("code", code)
			.queryParam("state", state)
			.toUriString();
	}

	private OauthUser getUser(String accessToken) {
		HttpEntity<String> requestEntity = userRequestEntity(accessToken);

		ResponseEntity<ResponseNaverUser> responseEntity =
			restTemplate.exchange(getUserApiUrl, HttpMethod.GET, requestEntity, ResponseNaverUser.class);

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new OAuthResourceAccessFailureException(this, accessToken);
		}

		ResponseNaverUser body = responseEntity.getBody();
		NaverUserInfo naverUserInfo = body.naverUserInfo();
		return new OauthUser(naverUserInfo.name(), naverUserInfo.email());
	}

	private HttpEntity<String> userRequestEntity(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
		return requestEntity;
	}
}
