package tback.kicketingback.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.performance.dto.MyReservationsResponse;
import tback.kicketingback.performance.service.ReservationService;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.dto.request.AddressRequest;
import tback.kicketingback.user.dto.request.InformRequest;
import tback.kicketingback.user.dto.request.NameRequest;
import tback.kicketingback.user.dto.request.PasswordRequest;
import tback.kicketingback.user.dto.response.UserResponse;
import tback.kicketingback.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final ReservationService reservationService;

	@GetMapping
	public ResponseEntity<UserResponse> getUser(@JwtLogin User user) {

		return ResponseEntity.ok(UserResponse.from(user));
	}

	@GetMapping("/my-reservation")
	public ResponseEntity<MyReservationsResponse> getMyReservationAll(@JwtLogin User user) {

		MyReservationsResponse myReservations = new MyReservationsResponse(userService.myReservations(user.getId()));

		return ResponseEntity.ok(myReservations);
	}

	@PutMapping("/address")
	public ResponseEntity<Void> updateAddress(@JwtLogin User user, @RequestBody AddressRequest addressRequest) {

		userService.updateAddress(user, addressRequest.address());

		return ResponseEntity.ok().build();
	}

	@PutMapping("/password")
	public ResponseEntity<Void> changePassword(@JwtLogin User user, @RequestBody PasswordRequest passwordRequest) {

		userService.matchPassword(user, passwordRequest.confirmPassword());
		userService.changePassword(user, passwordRequest.newPassword());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/password-recovery")
	public ResponseEntity<Void> matchInform(@JwtLogin User user, @RequestBody InformRequest informRequest) {

		userService.matchInform(user, informRequest.email(), informRequest.name());
		userService.setRandomPassword(user, user.getEmail());

		return ResponseEntity.ok().build();
	}

	@PutMapping("/name")
	public ResponseEntity<Void> updateName(@JwtLogin User user, @RequestBody NameRequest nameRequest) {

		userService.updateName(user, nameRequest.name());

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/my-reservation/{orderNumber}")
	public ResponseEntity<Void> deleteMyReservation(
		@JwtLogin User user,
		@PathVariable("orderNumber") String orderNumber
	) {
		reservationService.cancelReservation(user, orderNumber);

		return ResponseEntity.ok().build();
	}

}
