package tback.kicketingback.user.signup.service;

import static tback.kicketingback.auth.oauth.util.PasswordUtil.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.domain.UserState;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;

@Service
@RequiredArgsConstructor
@Qualifier("DefaultSignUpService")
public class DefaultSignUpService implements SignUpService {

	private final UserRepository userRepository;
	private final EmailAuthService emailAuthService;

	@Override
	public void signUp(SignUpRequest signUpRequest) {
		if (!isPasswordFormat(signUpRequest.password())) {
			throw new AuthInvalidPasswordException();
		}

		emailAuthService.validateEmailAuthAttempt(signUpRequest.email());

		User user = User.of(signUpRequest.email(), signUpRequest.password(), signUpRequest.name(),
			UserState.REGULAR_USER);
		if (userRepository.existsByEmail(signUpRequest.email())) {
			throw new EmailDuplicatedException();
		}

		userRepository.save(user);
		emailAuthService.expireEmailAuth(signUpRequest.email());
	}
}
