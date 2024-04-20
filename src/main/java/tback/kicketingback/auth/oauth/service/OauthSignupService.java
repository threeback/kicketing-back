package tback.kicketingback.auth.oauth.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;
import tback.kicketingback.user.signup.mail.SmtpService;
import tback.kicketingback.user.signup.service.SignUpService;

@Service
@Qualifier("OauthSignupService")
@RequiredArgsConstructor
public class OauthSignupService implements SignUpService {

	private final UserRepository userRepository;
	private final SmtpService smtpService;

	@Override
	public void signUp(SignUpRequest signUpRequest) {
		User user = User.of(signUpRequest.email(), signUpRequest.password(), signUpRequest.name());
		userRepository.save(user);
		smtpService.sendRandomPassword(user.getEmail(), user.getPassword());
	}
}
