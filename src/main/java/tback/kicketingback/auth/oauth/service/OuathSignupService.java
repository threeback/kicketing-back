package tback.kicketingback.auth.oauth.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.mail.SmtpService;
import tback.kicketingback.user.signup.service.SignUpService;

@Service
@Qualifier("OuathSignupService")
@RequiredArgsConstructor
public class OuathSignupService implements SignUpService {

	private final UserRepository userRepository;
	private final SmtpService smtpService;

	@Override
	public void signUp(String name, String email, String password) {
		User user = User.of(email, encode(password), name);
		userRepository.save(user);
		smtpService.sendRandomPassword(email, password);
	}
}
