package tback.kicketingback.auth.oauth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;

@Service
public class OauthSignInService {

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private final RedisRepository redisRepository;

	public OauthSignInService(
		UserRepository userRepository,
		JwtTokenProvider jwtTokenProvider,
		@Qualifier("refreshRedisRepository") RedisRepository refreshRedisRepository
	) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.redisRepository = refreshRedisRepository;
	}

	public TokenResponse signInUser(String email) {
		userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);

		String accessToken = jwtTokenProvider.generateAccessToken(email);
		String refreshToken = jwtTokenProvider.generateRefreshToken(email);

		Duration expiryDuration = jwtTokenProvider.getRefreshTokenExpiryDurationFromNow();
		redisRepository.setValues(email, refreshToken, expiryDuration);

		return TokenResponse.of(accessToken, refreshToken);
	}
}
