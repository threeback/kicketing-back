package tback.kicketingback.user.signup.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;
import static tback.kicketingback.user.domain.User.*;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Qualifier("DefaultSignUpService")
public class DefaultSignUpService implements SignUpService {

	private final UserRepository userRepository;
	private final EmailAuthService emailAuthService;

	private boolean isPasswordFormat(final String password) {
		return Pattern.matches(DEFAULT_PASSWORD_REGEX, password);
	}

	@Override
	public void signUp(String name, String email, String password) {
		if (!isPasswordFormat(password)) {
			throw new AuthInvalidPasswordException();
		}

		emailAuthService.validateEmailAuthAttempt(email);

		User user = User.of(email, encode(password), name);
		if (userRepository.existsByEmail(email)) {
			throw new EmailDuplicatedException();
		}

		userRepository.save(user);
		emailAuthService.expireEmailAuth(email);
	}
}
