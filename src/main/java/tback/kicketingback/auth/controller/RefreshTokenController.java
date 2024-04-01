package tback.kicketingback.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.exception.exceptions.ExpiredTokenException;
import tback.kicketingback.auth.exception.exceptions.InvalidJwtTokenException;
import tback.kicketingback.auth.jwt.JwtTokenExtractor;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.auth.jwt.JwtTokenType;
import tback.kicketingback.global.repository.RedisRepository;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class RefreshTokenController {

	private final JwtTokenExtractor jwtTokenExtractor;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisRepository redisRepository;

	@PostMapping("/refresh")
	public ResponseEntity refreshAccessToken(
		@RequestBody final String refreshToken,
		HttpServletResponse response
	) {
		String extractedRefreshToken = jwtTokenExtractor.extractRefreshToken(refreshToken);
		String email = jwtTokenProvider.extractEmailFromRefreshToken(extractedRefreshToken);

		String refreshTokenFromCash = redisRepository.getValues(email)
			.orElseThrow(() -> new ExpiredTokenException(JwtTokenType.REFRESH_TOKEN));

		if (!refreshTokenFromCash.equals(refreshToken)) {
			redisRepository.deleteValues(email);
			throw new InvalidJwtTokenException(JwtTokenType.REFRESH_TOKEN);
		}

		String newAccessToken = jwtTokenProvider.generateAccessToken(email);

		response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
		return ResponseEntity.ok().body("액서스 토큰 재발급 성공");
	}
}

