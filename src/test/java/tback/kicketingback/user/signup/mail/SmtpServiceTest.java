package tback.kicketingback.user.signup.mail;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.signup.service.SignUpEmailService;
import tback.kicketingback.user.signup.utils.NumberUtil;

@SpringBootTest
class SmtpServiceTest {

	@Autowired
	private SmtpService smtpService;

	@Autowired
	@Qualifier("signupRedisRepository")
	private RedisRepository signupRedisRepository;

	@Autowired
	private SignUpEmailService signUpEmailService;

	private final String TEST_EMAIL = "pwrwpw.dev@gmail.com";

	@BeforeEach
	void initBefore() {
		signupRedisRepository.deleteValues(TEST_EMAIL);
	}

	@Test
	@DisplayName("유저 이메일에게 인증 코드를 전송할 수 있다.")
	public void 이메일_정상_전송() {
		String code = NumberUtil.createRandomCode6();
		assertDoesNotThrow(() -> {
			smtpService.sendVerification(TEST_EMAIL, code);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = {"123d", "82fhaueir", "1", "!$#$^#$^"})
	@DisplayName("이메일 형식이 아니라면 예외를 던진다.")
	public void 이메일_형식이_아닐경우_예외(String email) {
		String code = NumberUtil.createRandomCode6();

		assertThrows(EmailFormatException.class, () -> smtpService.sendVerification(email, code));
	}

	@Test
	@DisplayName("이메일 전송에 성공하면 레디스에 인증코드가 있다.")
	public void 이메일_전송_성공시_레디스_확인() {
		String code = NumberUtil.createRandomCode6();

		smtpService.sendVerification(TEST_EMAIL, code);
		signUpEmailService.saveCode(TEST_EMAIL, code);

		assertEquals(code, signupRedisRepository.getValues(TEST_EMAIL).orElse("-1"));
	}

}