package tback.kicketingback.auth.oauth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final UserRepository userRepository;

	public boolean checkOurUser(OauthClient oauthClient, String authCode) {
		OauthUser oauthUser = oauthClient.getOauthUser(authCode);

		// return userRepository.ex(oauthUser.email());
		return true;
	}
}
