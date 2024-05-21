package tback.kicketingback.user.service;

import static tback.kicketingback.auth.oauth.util.PasswordUtil.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.performance.dto.DetailReservationDTO;
import tback.kicketingback.performance.exception.exceptions.NoSuchReservationException;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.domain.UserState;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidStateException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signup.mail.SmtpService;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ReservationRepositoryCustom reservationRepositoryCustom;
	private final SmtpService smtpService;

	public void checkEmailDuplication(final String email) {
		if (userRepository.existsByEmail(email)) {
			throw new EmailDuplicatedException();
		}
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

	@Transactional
	public void setRandomPassword(User user, String email) {
		String newPassword = PasswordUtil.createRandomPassword();
		smtpService.sendRandomPassword(email, newPassword);
		user.changePassword(newPassword);
	}

	@Transactional
	public void updateName(User user, String newName) {
		if (!Objects.equals(user.getState(), UserState.OAUTH_USER.getState()))
			throw new AuthInvalidStateException();
		user.updateName(newName);
		user.updateStateRegular(UserState.REGULAR_USER);
	}

	public void matchPassword(User user, String confirmPassword) {
		user.validatePassword(confirmPassword);
	}

	public void matchInform(User user, String email, String name) {

		user.validateEmail(email);
		user.validateName(name);
	}

	public List<DetailReservationDTO> myReservations(Long userId) {

		List<DetailReservationDTO> reservations = reservationRepositoryCustom.myReservations(userId);

		if (reservations.isEmpty()) {
			throw new NoSuchReservationException();
		}

		return reservations;
	}
}
