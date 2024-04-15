package tback.kicketingback.auth.oauth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.domain.OauthClientProvider;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.exception.exceptions.UnsupportedOAuthDomainException;
import tback.kicketingback.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final OauthClientProvider oauthClientProvider;
	private final UserRepository userRepository;

	public OauthUser getOauthUser(final String domain, final String authCode, final String state) {
		OauthClient oauthClient = oauthClientProvider.getOauthClient(domain)
			.orElseThrow(() -> new UnsupportedOAuthDomainException(domain));
		return oauthClient.getOauthUser(authCode, state);
	}

	public boolean checkOurUser(OauthUser oauthUser) {
		return userRepository.existsByEmail(oauthUser.email());
	}
}
