package tback.kicketingback.user.signup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.mail.internet.MimeMessage;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.AlreadyEmailAuthCompleteException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.EmailSendException;
import tback.kicketingback.user.exception.exceptions.MismatchEmailAuthCodeException;
import tback.kicketingback.user.signup.controller.SignUpController;
import tback.kicketingback.user.signup.dto.request.EmailCodeRequest;
import tback.kicketingback.user.signup.dto.request.EmailConfirmRequest;
import tback.kicketingback.user.signup.service.SignUpEmailService;
import tback.kicketingback.user.signup.utils.NumberUtil;

@SpringBootTest
public class UserSignUpEmailAuthTest {

	@Autowired
	@Qualifier("signupRedisRepository")
	private RedisRepository signupRedisRepository;

	@Autowired
	private SignUpController signUpController;

	private final String TEST_EMAIL = "pos06098@gmail.com";

	@BeforeEach
	void initBefore() {
		signupRedisRepository.deleteValues(TEST_EMAIL);
	}

	@Test
	@DisplayName("이미 이메일 인증을 완료하면 예외를 던진다.")
	public void alreadyCompleteEmailAuth() {
		EmailCodeRequest emailCodeRequest = new EmailCodeRequest(TEST_EMAIL);

		signupRedisRepository.setValues(TEST_EMAIL, "access");

		assertThatThrownBy(() -> signUpController.emailCode(emailCodeRequest))
			.isInstanceOf(AlreadyEmailAuthCompleteException.class);
	}

	@Test
	@DisplayName("인증 코드가 일치하면 상태가 변경된다.")
	public void signupAuthComplete() {
		String code = NumberUtil.createRandomCode6();
		EmailConfirmRequest emailConfirmRequest = new EmailConfirmRequest(TEST_EMAIL, code);

		signupRedisRepository.setValues(TEST_EMAIL, code);

		assertThatCode(() -> signUpController.emailConfirm(emailConfirmRequest)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("이메일이 틀린 경우")
	public void 이메일_틀림() {
		String code = NumberUtil.createRandomCode6();
		EmailConfirmRequest emailConfirmRequest = new EmailConfirmRequest(TEST_EMAIL, code);

		signupRedisRepository.setValues("noEmail@gmail.com", code);

		assertThatThrownBy(() -> signUpController.emailConfirm(emailConfirmRequest))
			.isInstanceOf(AuthInvalidEmailException.class);
	}

	@Test
	@DisplayName("이메일이 없는 경우")
	public void 이메일_없음() {
		String code = NumberUtil.createRandomCode6();
		EmailConfirmRequest emailConfirmRequest = new EmailConfirmRequest(TEST_EMAIL, code);

		assertThatThrownBy(() -> signUpController.emailConfirm(emailConfirmRequest))
			.isInstanceOf(AuthInvalidEmailException.class);
	}

	@Test
	@DisplayName("인증번호가 틀린 경우")
	public void 인증번호_틀림() {
		String code = NumberUtil.createRandomCode6();
		EmailConfirmRequest emailConfirmRequest = new EmailConfirmRequest(TEST_EMAIL, "000000");

		signupRedisRepository.setValues(TEST_EMAIL, code);

		assertThatThrownBy(() -> signUpController.emailConfirm(emailConfirmRequest))
			.isInstanceOf(MismatchEmailAuthCodeException.class);
	}
}
