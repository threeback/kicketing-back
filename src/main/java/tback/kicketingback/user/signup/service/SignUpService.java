package tback.kicketingback.user.signup.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;

@Service
@RequiredArgsConstructor
public class SignUpService {

	private final UserRepository userRepository;

	public void signUp(SignUpRequest signUpRequest) {

		final String email = signUpRequest.email();
		final String password = encode(signUpRequest.password());
		final String username = signUpRequest.username();

		if (!isPasswordFormat(signUpRequest.password())) {
			throw new AuthInvalidPasswordException();
		}
		User user = User.of(email, password, username);
		if (userRepository.existsByEmail(email)) {
			throw new EmailDuplicatedException();
		}

		userRepository.save(user);
	}

	private boolean isPasswordFormat(final String password) {
		return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$", password);
	}
}
