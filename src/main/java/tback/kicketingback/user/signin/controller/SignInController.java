package tback.kicketingback.user.signin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SignInController {

	@Value("${jwt.access.expiration}")
	private int EXPIRATION_TIME;

	private final SignInService signInService;

	@PostMapping("/sign-in")
	public ResponseEntity<String> signIn(@RequestBody @Valid SignInRequest signInRequest, HttpServletResponse response) {
		TokenResponse tokenResponse = signInService.signInUser(signInRequest);

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

