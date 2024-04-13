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
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.user.signin.dto.SignInRequest;
import tback.kicketingback.user.signin.service.SignInService;
import tback.kicketingback.user.signup.service.SignUpService;

@RestController
@RequestMapping("/api/oauth")
public class CallBackController {

	private final OauthClientService oauthClientService;

	private final SignUpService ouathSignupService;

	private final SignInService signInService;

	public CallBackController(OauthClientService oauthClientService,
		@Qualifier("OuathSignupService") SignUpService ouathSignupService,
		SignInService signInService) {
		this.oauthClientService = oauthClientService;
		this.ouathSignupService = ouathSignupService;
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
		//1. 회원 아니면 db저장 회원가입처리 (비밀번호는 난수 생성 후 이메일 발송)
		if (!oauthClientService.checkOurUser(oauthUser)) {
			ouathSignupService.signUp(oauthUser.name(), oauthUser.email(), password);
		}

		TokenResponse tokenResponse = signInService.signInUser(new SignInRequest(oauthUser.email(), password));
		response.setHeader(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken());

		return ResponseEntity.ok().body(tokenResponse.refreshToken());
		// return ResponseEntity.ok().build(); //프론트에서 응답받고 로그인 api로 요청
	}
}
