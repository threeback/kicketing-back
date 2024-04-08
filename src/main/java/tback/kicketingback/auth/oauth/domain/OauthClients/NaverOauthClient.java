package tback.kicketingback.auth.oauth.domain.OauthClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;

@Component
@RequiredArgsConstructor
public class NaverOauthClient implements OauthClient {

	private final RestTemplate restTemplate;

	@Value("naver.get.user.url")
	String apiUrl;

	@Override
	public OauthUser getOauthUser(String authCode) {
		String header = "Bearer " + authCode; // Bearer 다음에 공백 추가

		String responseEntity = getResponseEntity(header);

		return null;
	}

	private String getResponseEntity(String header) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", header);

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class, httpHeaders);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new OAuthResourceAccessFailureException();
		}

		return responseEntity.getBody();
	}
}
