package tback.kicketingback.user.signup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.domain.User;
import tback.kicketingback.user.domain.UserState;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailAuthIncompleteException;
import tback.kicketingback.user.exception.exceptions.EmailDuplicatedException;
import tback.kicketingback.user.repository.FakeUserRepository;
import tback.kicketingback.user.signup.dto.request.SignUpRequest;
import tback.kicketingback.user.signup.service.DefaultSignUpService;

@SpringBootTest
public class SignupTest {
	@Autowired
	private EmailAuthService emailAuthService;

	@Autowired
	@Qualifier("signupRedisRepository")
	private RedisRepository signupRedisRepository;

	private final ConcurrentHashMap<String, User> map = new ConcurrentHashMap();
	private FakeUserRepository fakeUserRepository;
	private DefaultSignUpService defaultSignUpService;

	private final String TEST_EMAIL = "test@test.com";
	private final String TEST_NAME = "john";
	private final String TEST_PASSWORD = "1234abc!@";

	@BeforeEach
	void initBefore() {
		map.clear();
		fakeUserRepository = new FakeUserRepository(map);
		defaultSignUpService = new DefaultSignUpService(fakeUserRepository, emailAuthService);
	}

	@Test
	@DisplayName("회원가입 정상 처리 후 디비에 유저가 존재한다.")
	public void 회원가입_정상_처리_디비() {
		emailAuthService.saveCode(TEST_EMAIL, "access");
		defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD));

		assertThat(fakeUserRepository.existsByEmail(TEST_EMAIL)).isTrue();
	}

	@Test
	@DisplayName("회원가입 정상 처리 후 회원가입 레디스에 인증 정보가 삭제된다.")
	public void 회원가입_정상_처리_레디스() {
		emailAuthService.saveCode(TEST_EMAIL, "access");
		defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD));

		assertThat(fakeUserRepository.existsByEmail(TEST_EMAIL)).isTrue();
		assertThat(signupRedisRepository.getValues(TEST_EMAIL).isEmpty()).isTrue();
	}

	@Test
	@DisplayName("유저 이메일 중복이면 예외가 발생한다.")
	public void 이메일_중복일_경우_예외() {
		map.put("test@test.com", User.of(TEST_EMAIL, "123456a!!", "beach", UserState.REGULAR_USER));
		emailAuthService.saveCode(TEST_EMAIL, "access");

		assertThrows(EmailDuplicatedException.class,
			() -> defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD)));
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "123test12", "test@@@@@", "123123123!", "1a@", "1a@12312!@#!@!#!@#@!#@!2!@!!zda",
		"1a@12312!@#!@!#!@#@!#@!2!@!!zda"})
	@DisplayName("비밀번호가 형식에 맞지 않는 경우")
	public void 비밀번호_형식_맞지않음(String password) {
		emailAuthService.saveCode(TEST_EMAIL, "access");

		assertThrows(AuthInvalidPasswordException.class,
			() -> defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, password)));
	}

	@Test
	@DisplayName("이메일 인증을 시도하지 않은 경우")
	public void 회원가입_전_이메일_인증_안함() {
		assertThrows(EmailAuthIncompleteException.class,
			() -> defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD)));
	}

	@Test
	@DisplayName("이메일 인증을 완료하지 않은 경우")
	public void 회원가입_전_이메일_인증_완료_안함() {
		emailAuthService.saveCode(TEST_EMAIL, "123123");

		assertThrows(EmailAuthIncompleteException.class,
			() -> defaultSignUpService.signUp(new SignUpRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD)));
	}
}
