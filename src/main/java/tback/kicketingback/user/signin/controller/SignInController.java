package tback.kicketingback.user.signin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.user.signin.dto.SignInRequest;
import tback.kicketingback.user.signin.service.SignInService;

@RestController
@RequestMapping("/api/user/sign-in")
@RequiredArgsConstructor
public class SignInController {

	@Value("${jwt.access.expiration}")
	private int EXPIRATION_TIME;

	private final SignInService signInService;

	@PostMapping
	public ResponseEntity<String> signIn(@RequestBody @Valid SignInRequest signInRequest,
		HttpServletResponse response) {
		TokenResponse tokenResponse = signInService.signInUser(signInRequest);

		tokenResponse.setAccessToken(response, EXPIRATION_TIME);
		return ResponseEntity.ok().body(tokenResponse.refreshToken());
	}
}

