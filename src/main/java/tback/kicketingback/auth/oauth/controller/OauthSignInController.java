package tback.kicketingback.auth.oauth.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.oauth.dto.OauthUser;
import tback.kicketingback.auth.oauth.dto.RequestCallBack;
import tback.kicketingback.auth.oauth.service.OauthClientService;
import tback.kicketingback.auth.oauth.service.OauthSignInService;
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.user.signup.service.SignUpService;

@RestController
@RequestMapping("/api/oauth")
public class OauthSignInController {

	private final OauthClientService oauthClientService;

	private final SignUpService oauthSignupService;

	private final OauthSignInService oauthSignInService;

	public OauthSignInController(OauthClientService oauthClientService,
		@Qualifier("OauthSignupService") SignUpService oauthSignupService,
		OauthSignInService oauthSignInService) {
		this.oauthClientService = oauthClientService;
		this.oauthSignupService = oauthSignupService;
		this.oauthSignInService = oauthSignInService;
	}

	@PostMapping("/{domain}")
	public ResponseEntity<String> loginOAuth(
		@PathVariable("domain") final String domain,
		@RequestBody final RequestCallBack requestCallBack,
		HttpServletResponse response
	) {
		OauthUser oauthUser = oauthClientService.getOauthUser(
			domain,
			requestCallBack.authCode(),
			requestCallBack.state());

		if (!oauthClientService.checkOurUser(oauthUser)) {
			oauthSignupService.signUp(oauthUser.name(), oauthUser.email(), PasswordUtil.createRandomPassword());
		}

		TokenResponse tokenResponse = oauthSignInService.signInUser(oauthUser.email());
		response.setHeader(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken());

		return ResponseEntity.ok().body(tokenResponse.refreshToken());
	}
}
