package tback.kicketingback.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tback.kicketingback.auth.exception.exceptions.ExpiredTokenException;
import tback.kicketingback.auth.exception.exceptions.InvalidJwtTokenException;
import tback.kicketingback.auth.jwt.JwtTokenType;
import tback.kicketingback.global.repository.RedisRepository;

@Service
public class RefreshTokenService {

	private final RedisRepository redisRepository;

	public RefreshTokenService(@Qualifier("refreshRedisRepository") RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	public void validateRefreshToken(String email, String extractedRefreshToken) {
		String refreshTokenFromCash = redisRepository.getValues(email)
			.orElseThrow(() -> new ExpiredTokenException(JwtTokenType.REFRESH_TOKEN));

		if (!refreshTokenFromCash.equals(extractedRefreshToken)) {
			redisRepository.deleteValues(email);
			throw new InvalidJwtTokenException(JwtTokenType.REFRESH_TOKEN);
		}
	}
}
