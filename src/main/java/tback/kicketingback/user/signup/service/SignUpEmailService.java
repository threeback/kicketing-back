package tback.kicketingback.user.signup.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.AlreadyEmailAuthCompleteException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.EmailAuthIncompleteException;
import tback.kicketingback.user.exception.exceptions.MismatchEmailAuthCodeException;

@Service
public class SignUpEmailService implements EmailAuthService {
	private final RedisRepository signupRedisRepository;

	@Value("${spring.data.redis.timeout.signup.code}")
	private int codeExpireTime;

	@Value("${spring.data.redis.timeout.signup.access}")
	private int accessExpireTime;

	public SignUpEmailService(
		@Qualifier("signupRedisRepository") RedisRepository signupRedisRepository
	) {
		this.signupRedisRepository = signupRedisRepository;
	}
	@Override
	public void saveCode(String email, String code) {
		signupRedisRepository.setValues(email, code, Duration.ofMillis(codeExpireTime));
	}

	@Override
	public void validateEmailAuthCompletion(String email) {
		Optional<String> state = signupRedisRepository.getValues(email);

		if (state.isEmpty()) {
			throw new AuthInvalidEmailException();
		}
		if (state.get().equals(EMAIL_AUTH_ACCESS)) {
			throw new AlreadyEmailAuthCompleteException();
		}
	}

	@Override
	public void validateEmailAuthAttempt(String email) {
		Optional<String> state = signupRedisRepository.getValues(email);

		if (state.isEmpty() || !state.get().equals(EMAIL_AUTH_ACCESS)) {
			throw new EmailAuthIncompleteException();
		}
	}

	@Override
	public void checkCode(String email, String inputCode) {
		String code = signupRedisRepository.getValues(email).orElseThrow(AuthInvalidEmailException::new);

		if (!code.equals(inputCode)) {
			throw new MismatchEmailAuthCodeException();
		}

		signupRedisRepository.setValues(email, EMAIL_AUTH_ACCESS, Duration.ofMillis(accessExpireTime));
	}

	@Override
	public void expireEmailAuth(String email) {
		signupRedisRepository.deleteValues(email);
	}
}
