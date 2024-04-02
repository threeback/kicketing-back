package tback.kicketingback.user.signin.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.user.signin.dto.SignInRequest;
import tback.kicketingback.user.signin.service.SignInService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SignInController {

	private final SignInService signInService;

	@PostMapping("/sign-in")
	public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
		//로그인 가능한지
		TokenResponse tokenResponse = signInService.signInUser(signInRequest);

		response.setHeader(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken());
		return ResponseEntity.ok().body(tokenResponse.refreshToken());
	}
}
