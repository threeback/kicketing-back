package tback.kicketingback.auth.oauth.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.user.signin.dto.SignInRequest;
import tback.kicketingback.user.signin.service.SignInService;
import tback.kicketingback.user.signup.service.SignUpService;

@RestController
@RequestMapping("/api/oauth")
public class OauthSignInController {

	@Value("${jwt.access.expiration}")
	private int EXPIRATION_TIME;

	private final OauthClientService oauthClientService;

	private final SignUpService oauthSignupService;

	private final SignInService signInService;

	public OauthSignInController(OauthClientService oauthClientService,
		@Qualifier("OauthSignupService") SignUpService oauthSignupService,
		SignInService signInService) {
		this.oauthClientService = oauthClientService;
		this.oauthSignupService = oauthSignupService;
		this.signInService = signInService;
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

		String password = PasswordUtil.createRandomPassword();
		if (!oauthClientService.checkOurUser(oauthUser)) {
			oauthSignupService.signUp(oauthUser.name(), oauthUser.email(), password);
		}

		TokenResponse tokenResponse = signInService.signInUser(new SignInRequest(oauthUser.email(), password));

		ResponseCookie accessTokenCookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(EXPIRATION_TIME)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
		return ResponseEntity.ok().body(tokenResponse.refreshToken());
	}
}
