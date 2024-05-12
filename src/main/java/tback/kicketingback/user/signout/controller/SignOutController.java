package tback.kicketingback.user.signout.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtLogin;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.signout.service.SignOutService;

@RestController
@RequestMapping("/api/user/sign-out")
@RequiredArgsConstructor
public class SignOutController {

	private final SignOutService signOutService;

	@PostMapping
	public ResponseEntity<Void> signOut(@JwtLogin User user, HttpServletResponse response) {
		signOutService.signOutUser(user);
		TokenResponse.expireAccessToken(response);

		return ResponseEntity.ok().build();
	}
}
