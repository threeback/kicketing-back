package tback.kicketingback.user.signin.service;

import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signin.dto.SignInRequest;

@Service
public class SignInService {

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private final RedisRepository redisRepository;

	public SignInService(
		UserRepository userRepository,
		JwtTokenProvider jwtTokenProvider,
		@Qualifier("refreshRedisRepository") RedisRepository refreshRedisRepository
	) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.redisRepository = refreshRedisRepository;
	}

	public TokenResponse signInUser(SignInRequest signInRequest) {
		String email = signInRequest.email();
		User user = userRepository.findByEmail(email)
			.orElseThrow(NoSuchUserException::new);

		String encodedPassword = encode(signInRequest.password());
		user.validatePassword(encodedPassword);

		String accessToken = jwtTokenProvider.generateAccessToken(email);
		String refreshToken = jwtTokenProvider.generateRefreshToken(email);

		Duration expiryDuration = jwtTokenProvider.getRefreshTokenExpiryDurationFromNow();
		redisRepository.setValues(email, refreshToken, expiryDuration);

		return TokenResponse.of(accessToken, refreshToken);
	}
}
