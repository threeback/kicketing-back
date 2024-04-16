package tback.kicketingback.user.signin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
		TokenResponse tokenResponse = signInService.signInUser(signInRequest);

		Cookie accessTokenCookie = new Cookie(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken());
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setSecure(true);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setMaxAge(EXPIRATION_TIME);

		response.addCookie(accessTokenCookie);
		return ResponseEntity.ok().body(tokenResponse.refreshToken());
	}
}

