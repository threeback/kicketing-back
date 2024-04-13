package tback.kicketingback.user.signup.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;

@Service
public class DefaultSignUpService implements SignUpService {

	private final UserRepository userRepository;
	private final EmailAuthService emailAuthService;
	private final static String DEFAULT_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$";

	public DefaultSignUpService(UserRepository userRepository, EmailAuthService emailAuthService) {
		this.userRepository = userRepository;
		this.emailAuthService = emailAuthService;
	}

	@Override
	public void signUp(SignUpRequest signUpRequest) {
		if (!isPasswordFormat(signUpRequest.password())) {
			throw new AuthInvalidPasswordException();
		}

		emailAuthService.validateEmailAuthAttempt(signUpRequest.email());

		User user = User.of(signUpRequest.email(), encode(signUpRequest.password()), signUpRequest.name());
		if (userRepository.existsByEmail(signUpRequest.email())) {
			throw new EmailDuplicatedException();
		}

		userRepository.save(user);
		emailAuthService.expireEmailAuth(signUpRequest.email());
	}

	private boolean isPasswordFormat(final String password) {
		return Pattern.matches(DEFAULT_PASSWORD_REGEX, password);
	}
}
