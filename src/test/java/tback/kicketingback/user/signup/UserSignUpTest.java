package tback.kicketingback.user.signup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.mail.internet.MimeMessage;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.EmailSendException;
import tback.kicketingback.user.signup.service.SignUpEmailService;
import tback.kicketingback.user.signup.utils.NumberUtil;

@SpringBootTest
public class UserSignUpTest {

	@Autowired
	private SignUpEmailService emailService;

	@Autowired
	@Qualifier("signupRedisRepository")
	RedisRepository signupRedisRepository;

	@Test
	@DisplayName("유저 이메일 검증 코드 전송할 수 있다.")
	public void emailVerificationTest() {
		String email = "pos06098@gmail.com";
		int number = NumberUtil.createNumber();
		Assertions.assertDoesNotThrow(() -> {
			MimeMessage message = emailService.createMail(email, emailService.createBody(number));
			emailService.sendMail(message, number);
		});
	}

	@Test
	@DisplayName("없는 이메일에 전송했을 때 예외를 던진다.")
	public void emailVerificationTest2() {
		String email = "1234A";
		int number = NumberUtil.createNumber();

		Assertions.assertThrows(EmailSendException.class, () -> {
			MimeMessage message = emailService.createMail(email, emailService.createBody(number));
			emailService.sendMail(message, number);
		});
	}

	@Test
	@DisplayName("이메일 전송에 선공하면 레디스에 인증코드가 있다.")
	public void emailCodeInRedis() {
		String email = "pos06098@gmail.com";
		int number = NumberUtil.createNumber();

		MimeMessage message = emailService.createMail(email, emailService.createBody(number));
		emailService.sendMail(message, number);

		Assertions.assertEquals(number, Integer.parseInt(signupRedisRepository.getValues(email).get()));
	}
}
