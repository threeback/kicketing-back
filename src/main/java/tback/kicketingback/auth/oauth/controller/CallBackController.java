package tback.kicketingback.auth.oauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.domain.OauthClient;
import tback.kicketingback.auth.oauth.domain.OauthClientProvider;
import tback.kicketingback.auth.oauth.exception.exceptions.OAuthResourceAccessFailureException;
import tback.kicketingback.auth.oauth.service.OauthClientService;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class CallBackController {
	private final OauthClientProvider oauthClientProvider;
	private final OauthClientService oauthClientService;

	@PostMapping("/{domain}")
	public ResponseEntity<Void> loginOAuth(@PathVariable final String domain, @RequestBody final String authCode) {
		OauthClient oauthClient = oauthClientProvider.getOauthClient(domain)
			.orElseThrow(OAuthResourceAccessFailureException::new);

		//1. 회원 아니면 db저장 회원가입처리 (비밀번호는 난수 생성 후 이메일 발송)
		if (!oauthClientService.checkOurUser(oauthClient, authCode)) {
			// oauthSingupService.Signup()
		}
		return ResponseEntity.ok().build(); //프론트에서 응답받고 로그인 api로 요청
	}
}
