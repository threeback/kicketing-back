package tback.kicketingback.user.signin;

import static org.assertj.core.api.Assertions.*;
import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.auth.dto.TokenResponse;
import tback.kicketingback.auth.jwt.JwtTokenProvider;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.NoSuchUserException;
import tback.kicketingback.user.repository.FakeUserRepository;
import tback.kicketingback.user.signin.dto.SignInRequest;
import tback.kicketingback.user.signin.service.SignInService;

@SpringBootTest
public class UserSignInTest {

	private SignInService signInService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	@Qualifier("refreshRedisRepository")
	private RedisRepository redisRepository;

	@BeforeEach
	void initRepository() {
		ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
		concurrentHashMap.put("test@test.com", User.of("test@test.com", encode("1234"), "test"));
		signInService = new SignInService(new FakeUserRepository(concurrentHashMap), jwtTokenProvider, redisRepository);
		redisRepository.deleteValues("test@test.com");
	}

	@Test
	@DisplayName("이메일로 유저를 찾지 못 하면 예외를 반환한다.")
	void 없는_이메일로_유저검색_테스트() {
		SignInRequest signInRequest = new SignInRequest("noName@test.com", "1234");
		assertThatThrownBy(() -> {
			signInService.signInUser(signInRequest);
		}).isInstanceOf(NoSuchUserException.class);
	}

	@Test
	@DisplayName("패드워드가 다르면 예외를 반환한다.")
	void 틀린_패드워드_테스트() {
		SignInRequest signInRequest = new SignInRequest("test@test.com", "1111111");
		assertThatThrownBy(() -> {
			signInService.signInUser(signInRequest);
		}).isInstanceOf(AuthInvalidPasswordException.class);
	}

	@Test
	@DisplayName("로그인 정상처리")
	void 이메일_존재_비밀번호_일치() {
		String email = "test@test.com";
		String password = "1234";
		SignInRequest signInRequest = new SignInRequest(email, password);

		//로그인 액서스, 리프레시 토큰을 가지는 response를 반환한다.
		TokenResponse tokenResponse = signInService.signInUser(signInRequest);

		//액서스, 리프레시 토큰이 유효한지.
		String extractEmailFromAccessToken = jwtTokenProvider.extractEmailFromAccessToken(tokenResponse.accessToken());
		assertThat(extractEmailFromAccessToken).isEqualTo(email);

		String extractEmailFromRefreshToken = jwtTokenProvider.extractEmailFromRefreshToken(
			tokenResponse.refreshToken());
		assertThat(extractEmailFromRefreshToken).isEqualTo(email);
	}

	@Test
	@DisplayName("로그인에 성공하면 레프레시 토큰을 저장한다.")
	void 로그인_성공시_리프레시_토큰_저장() {
		SignInRequest signInRequest = new SignInRequest("test@test.com", "1234");
		signInService.signInUser(signInRequest);

		System.out.println(redisRepository.getValues("test@test.com").orElse("empty"));
		assertThat(redisRepository.getValues("test@test.com").isEmpty()).isFalse();
	}

	@Test
	@DisplayName("로그인에 실패하면 레프레시 토큰을 저장하지 않는다.")
	void 로그인_실패시_리프레시_토큰_저장안함() {
		String email = new String("test@test.com");
		SignInRequest signInRequest = new SignInRequest(email, "1111111");
		assertThatThrownBy(() -> {
			signInService.signInUser(signInRequest);
		}).isInstanceOf(AuthInvalidPasswordException.class);

		String assertEmail = "test@test.com";
		System.out.println(redisRepository.getValues(assertEmail).orElse("empty"));
		assertThat(redisRepository.getValues("test@test.com").isEmpty()).isTrue();
	}
}
