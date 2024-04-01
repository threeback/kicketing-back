package tback.kicketingback.user.signin.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.UserRepository;
import tback.kicketingback.user.signin.dto.SignInRequest;

@Service
@RequiredArgsConstructor
public class SignInService {

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private final RedisRepository redisRepository;

	public TokenResponse signInUser(SignInRequest signInRequest) {
		String email = signInRequest.email();
		User user = userRepository.findByEmail(email)
			.orElseThrow(NoSuchUserException::new);

		String password = signInRequest.password();
		user.validatePassword(password);

		String accessToken = jwtTokenProvider.generateAccessToken(email);
		String refreshToken = jwtTokenProvider.generateRefreshToken(email);

		Duration expiryDuration = jwtTokenProvider.getRefreshTokenExpiryDurationFromNow();
		redisRepository.setValues(email, refreshToken, expiryDuration);

		return TokenResponse.of(accessToken, refreshToken);
	}
}
