package tback.kicketingback.user.signup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.mail.internet.MimeMessage;
import tback.kicketingback.user.exception.exceptions.EmailSendException;
import tback.kicketingback.user.signup.service.SignUpEmailService;

@SpringBootTest
public class UserSignUpTest {

	@Autowired
	private SignUpEmailService emailService;

	@Test
	@DisplayName("유저 이메일 검증 코드 전송할 수 있다.")
	public void emailVerificationTest() {
		String email = "pos06098@gmail.com";

		Assertions.assertDoesNotThrow(() -> {
			MimeMessage message = emailService.createMail(email);
			emailService.sendMail(message);
		});
	}

	@Test
	@DisplayName("없는 이메일에 전송했을 때 예외를 던진다.")
	public void emailVerificationTest2() {
		String email = "1234A";

		Assertions.assertThrows(EmailSendException.class, () -> {
			MimeMessage message = emailService.createMail(email);
			emailService.sendMail(message);
		});
	}
}
