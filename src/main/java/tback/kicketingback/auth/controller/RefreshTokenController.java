package tback.kicketingback.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.dto.RefreshTokenRequest;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.auth.service.RefreshTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
public class RefreshTokenController {

	@Value("${jwt.access.expiration}")
	private int EXPIRATION_TIME;

	private final JwtTokenProvider jwtTokenProvider;

	private final RefreshTokenService refreshTokenService;

	@PostMapping
	public ResponseEntity<Void> refreshAccessToken(
		@RequestBody @Valid final RefreshTokenRequest refreshTokenRequest,
		HttpServletResponse response
	) {
		String extractedRefreshToken = refreshTokenRequest.refreshToken();
		String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshTokenRequest.refreshToken());

		refreshTokenService.validateRefreshToken(email, extractedRefreshToken);

		String newAccessToken = jwtTokenProvider.generateAccessToken(email);

		TokenResponse.of(newAccessToken, refreshTokenRequest.refreshToken())
			.setAccessToken(response, EXPIRATION_TIME);

		return ResponseEntity.ok().build();
	}
}

