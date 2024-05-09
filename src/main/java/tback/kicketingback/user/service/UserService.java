package tback.kicketingback.user.service;

import static tback.kicketingback.auth.oauth.util.PasswordUtil.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User findUser(final String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(NoSuchUserException::new);
	}

	@Transactional
	public void updateAddress(User user, String address) {
		user.updateAddress(address);
	}

	@Transactional
	public void changePassword(User user, String password) {
		if (!isPasswordFormat(password)) {
			throw new AuthInvalidPasswordException();
		}

		user.changePassword(password);
	}

    public void matchPassword(User user, String confirmPassword) {
        user.validatePassword(confirmPassword);
    }

}
