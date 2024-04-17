package tback.kicketingback.user.signup.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.user.signup.dto.request.EmailCodeRequest;
import tback.kicketingback.user.signup.dto.request.EmailConfirmRequest;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;
import tback.kicketingback.user.signup.mail.SmtpService;
import tback.kicketingback.user.signup.service.SignUpService;
import tback.kicketingback.utils.NumberCodeUtil;

@RestController
@RequestMapping("/api/user/sign-up")
public class SignUpController {

	private final SignUpService defaultSignUpService;

	private final EmailAuthService signUpEmailAuthService;
	private final SmtpService smtpService;

	public SignUpController(
		@Qualifier("DefaultSignUpService") SignUpService defaultSignUpService,
		EmailAuthService signUpEmailAuthService,
		SmtpService smtpService) {
		this.defaultSignUpService = defaultSignUpService;
		this.signUpEmailAuthService = signUpEmailAuthService;
		this.smtpService = smtpService;
	}

	@PostMapping("/")
	public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
		defaultSignUpService.signUp(signUpRequest.name(), signUpRequest.email(), signUpRequest.password());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/code")
	public ResponseEntity<Void> emailCode(@RequestBody EmailCodeRequest emailCodeRequest) {
		String email = emailCodeRequest.email();

		signUpEmailAuthService.validateEmailAuthCompletion(email);

		String code = NumberCodeUtil.createRandomCode6();

		smtpService.sendVerification(email, code);
		signUpEmailAuthService.saveCode(email, code);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/confirm")
	public ResponseEntity<Void> emailConfirm(@RequestBody EmailConfirmRequest emailConfirmRequest) {
		signUpEmailAuthService.validateEmailAuthCompletion(emailConfirmRequest.email());

		signUpEmailAuthService.checkCode(emailConfirmRequest.email(), emailConfirmRequest.code());

		return ResponseEntity.ok().build();
	}
}
