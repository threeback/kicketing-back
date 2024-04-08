package tback.kicketingback.auth.oauth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.domain.OauthClientProvider;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;
import tback.kicketingback.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final OauthClientProvider oauthClientProvider;
	private final UserRepository userRepository;

	public boolean checkOurUser(String domain, String authCode) {
		OauthClient oauthClient = oauthClientProvider.provideOauthClient(domain)
			.orElseThrow(OAuthResourceAccessFailureException::new);

		OauthUser oauthUser = oauthClient.getOauthUser(authCode);

		// return userRepository.ex(oauthUser.email());
		return true;
	}
}
