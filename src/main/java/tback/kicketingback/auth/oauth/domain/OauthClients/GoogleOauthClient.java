package tback.kicketingback.auth.oauth.domain.OauthClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.dto.ResponseGoogleToken;
import tback.kicketingback.auth.oauth.dto.ResponseGoogleUser;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;

@Component
@RequiredArgsConstructor
public class GoogleOauthClient implements OauthClient {

	private static final String GET_TOKEN = "authorization_code";

	private final RestTemplate restTemplate;

	@Value("${google.client_id}")
	String clientId;

	@Value("${google.client_secret}")
	String clientSecret;

	@Value("${google.token.redirect.uri}")
	String redirectUri;

	@Value("${google.get.user.token.uri}")
	String getTokenApiUrl;

	@Value("${google.get.user.url}")
	String getUserApiUrl;

	@Override
	public OauthUser getOauthUser(String authCode, String state) {
		ResponseGoogleToken token = getToken(authCode);
		return getUser(token.accessToken());
	}

	private ResponseGoogleToken getToken(String authCode) {
		HttpEntity<MultiValueMap<String, String>> googleRequestEntity = getTokenRequestEntity(authCode);
		ResponseEntity<ResponseGoogleToken> response =
			restTemplate.exchange(getTokenApiUrl, HttpMethod.POST, googleRequestEntity, ResponseGoogleToken.class);
		return response.getBody();
	}

	private OauthUser getUser(String accessToken) {
		HttpEntity<String> requestEntity = userRequestEntity(accessToken);

		ResponseEntity<ResponseGoogleUser> responseEntity =
			restTemplate.exchange(getUserApiUrl, HttpMethod.GET, requestEntity, ResponseGoogleUser.class);

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new OAuthResourceAccessFailureException(this, accessToken);
		}

		ResponseGoogleUser body = responseEntity.getBody();
		return new OauthUser(body.name(), body.email());
	}

	private HttpEntity<MultiValueMap<String, String>> getTokenRequestEntity(String authCode) {
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("client_id", clientId);
		requestBody.add("client_secret", clientSecret);
		requestBody.add("code", authCode);
		requestBody.add("grant_type", GET_TOKEN);
		requestBody.add("redirect_uri", redirectUri);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		return new HttpEntity<>(requestBody, headers);
	}

	private HttpEntity<String> userRequestEntity(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

		return new HttpEntity<>(httpHeaders);
	}
}
