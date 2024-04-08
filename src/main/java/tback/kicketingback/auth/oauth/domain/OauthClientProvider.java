package tback.kicketingback.auth.oauth.domain;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthClientProvider {
	private final Map<String, OauthClient> oauthClients;

	public Optional<OauthClient> provideOauthClient(final String name) {
		return Optional.ofNullable(oauthClients.get(name));
	}
}
