package tback.kicketingback.auth.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import tback.kicketingback.auth.exception.exceptions.ExpiredTokenException;
import tback.kicketingback.auth.exception.exceptions.InvalidJwtTokenException;
import tback.kicketingback.auth.jwt.JwtTokenExtractor;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.auth.jwt.JwtTokenType;
import tback.kicketingback.global.repository.RedisRepository;

@RestController
@RequestMapping("/api")
public class RefreshTokenController {

	@Value("${jwt.access.expiration}")
	private int EXPIRATION_TIME;

	private final JwtTokenExtractor jwtTokenExtractor;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisRepository redisRepository;

	public RefreshTokenController(
		JwtTokenExtractor jwtTokenExtractor,
		JwtTokenProvider jwtTokenProvider,
		@Qualifier("refreshRedisRepository") RedisRepository refreshRedisRepository
	) {
		this.jwtTokenExtractor = jwtTokenExtractor;
		this.jwtTokenProvider = jwtTokenProvider;
		this.redisRepository = refreshRedisRepository;
	}

	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshAccessToken(
		@RequestBody @Valid final String refreshToken,
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

		Cookie newAccessTokenCookie = new Cookie(HttpHeaders.AUTHORIZATION, newAccessToken);
		newAccessTokenCookie.setHttpOnly(true);
		newAccessTokenCookie.setSecure(true);
		newAccessTokenCookie.setPath("/");
		newAccessTokenCookie.setMaxAge(EXPIRATION_TIME);

		response.addCookie(newAccessTokenCookie);
		return ResponseEntity.ok().build();
	}
}

