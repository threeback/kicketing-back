package tback.kicketingback.user.signup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;
import tback.kicketingback.user.signup.service.DefaultSignUpService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SignUpController {

	private final DefaultSignUpService defaultSignUpService;

	@PostMapping("/sign-up")
	public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {

		defaultSignUpService.signUp(signUpRequest);

		return ResponseEntity.ok().build();
	}
}
