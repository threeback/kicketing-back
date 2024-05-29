package tback.kicketingback.user.service;

import static tback.kicketingback.auth.oauth.util.PasswordUtil.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.oauth.util.PasswordUtil;
import tback.kicketingback.performance.domain.CanceledReservation;
import tback.kicketingback.performance.dto.DetailReservationDTO;
import tback.kicketingback.performance.dto.SeatDTO;
import tback.kicketingback.performance.exception.exceptions.NoSuchReservationException;
import tback.kicketingback.performance.repository.CanceledReservationRepository;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.domain.UserState;
import tback.kicketingback.user.dto.response.CanceledReservationDTO;
import tback.kicketingback.user.dto.response.MyReservationsInfoResponse;
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
	private final CanceledReservationRepository canceledReservationRepository;

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

	public Map<String, MyReservationsInfoResponse> myReservations(Long userId) {

		List<DetailReservationDTO> reservations = reservationRepositoryCustom.myReservations(userId);

		if (reservations.isEmpty()) {
			throw new NoSuchReservationException();
		}

		return reservations.stream()
			.collect(Collectors.groupingBy(
				reservation -> reservation.simpleReservationDTO().orderNumber(),
				Collectors.collectingAndThen(
					Collectors.toList(),
					reservationList -> {
						List<SeatDTO> seats = reservationList.stream()
							.map(DetailReservationDTO::seatDTO)
							.collect(Collectors.toList());
						return new MyReservationsInfoResponse(
							seats,
							reservationList.get(0).simpleReservationDTO(),
							reservationList.get(0).onStageDTO(),
							reservationList.get(0).simplePerformanceDTO(),
							reservationList.get(0).placeDTO()
						);
					}
				)
			));
	}

	public Map<String, CanceledReservationDTO> myCanceledInfo(Long userId) {
		List<CanceledReservation> canceledReservations = canceledReservationRepository.findByUserId(userId);

		return canceledReservations.stream()
			.collect(Collectors.groupingBy(CanceledReservation::getOrderNumber,
				Collectors.collectingAndThen(
					Collectors.toList(),
					canceledReservationList -> {
						List<SeatDTO> seats = canceledReservationList.stream()
							.map(canceledReservation -> new SeatDTO(canceledReservation.getGrade(),
								canceledReservation.getSeatRow(),
								canceledReservation.getSeatCol()))
							.toList();
						return new CanceledReservationDTO(
							canceledReservationList.get(0).getId(),
							canceledReservationList.get(0).getCanceledAt(),
							canceledReservationList.get(0).getOrderedAt(),
							canceledReservationList.get(0).getPerformanceName(),
							canceledReservationList.get(0).getImageUrl(),
							canceledReservationList.get(0).getPerformanceDate(),
							canceledReservationList.get(0).getRound(),
							canceledReservationList.get(0).getPlaceName(),
							canceledReservationList.get(0).getHall(),
							seats);
					})));
	}
}
