package tback.kicketingback.user.signup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.exception.exceptions.AlreadyEmailAuthCompleteException;
import tback.kicketingback.user.signup.dto.request.EmailCodeRequest;
import tback.kicketingback.user.signup.dto.request.EmailConfirmRequest;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;
import tback.kicketingback.user.signup.service.DefaultSignUpService;
import tback.kicketingback.user.signup.service.SignUpEmailService;
import tback.kicketingback.user.signup.utils.NumberUtil;

@RestController
@RequestMapping("/api/user/sign-up")
@RequiredArgsConstructor
public class SignUpController {

	private final DefaultSignUpService defaultSignUpService;
	private final SignUpEmailService signUpEmailService;

	@PostMapping("/")
	public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
		defaultSignUpService.signUp(signUpRequest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/code")
	public ResponseEntity<Void> emailCode(@RequestBody EmailCodeRequest emailCodeRequest) {
		String email = emailCodeRequest.email();

		if (signUpEmailService.isCompleteEmailAuth(email)) {
			throw new AlreadyEmailAuthCompleteException();
		}

		int code = NumberUtil.createRandomCode6();
		String body = signUpEmailService.createBody(email, String.valueOf(code));
		MimeMessage message = signUpEmailService.createMail(email, body);

		signUpEmailService.sendMail(message);
		signUpEmailService.saveCode(email, String.valueOf(code));

		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/confirm")
	public ResponseEntity<Void> emailConfirm(@RequestBody EmailConfirmRequest emailConfirmRequest) {
		if (signUpEmailService.isCompleteEmailAuth(emailConfirmRequest.email())) {
			throw new AlreadyEmailAuthCompleteException();
		}
		
		signUpEmailService.checkCode(emailConfirmRequest.email(), emailConfirmRequest.code());

		return ResponseEntity.ok().build();
	}
}
