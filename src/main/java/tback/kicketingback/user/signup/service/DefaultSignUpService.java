package tback.kicketingback.user.signup.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;

@Service
@RequiredArgsConstructor
public class DefaultSignUpService implements SignUpService {

	private final UserRepository userRepository;
	private final EmailAuthService emailAuthService;

	@Override
	public void signUp(SignUpRequest signUpRequest) {
		if (!isPasswordFormat(signUpRequest.password())) {
			throw new AuthInvalidPasswordException();
		}

		if (!emailAuthService.isCompleteEmailAuth(signUpRequest.email())) {
			throw new AuthInvalidEmailException();
		}

		User user = User.of(signUpRequest.email(), encode(signUpRequest.password()), signUpRequest.username());
		if (userRepository.existsByEmail(signUpRequest.email())) {
			throw new EmailDuplicatedException();
		}

		userRepository.save(user);
		emailAuthService.expireEmailAuth(signUpRequest.email());
	}

	private boolean isPasswordFormat(final String password) {
		return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$", password);
	}
}
