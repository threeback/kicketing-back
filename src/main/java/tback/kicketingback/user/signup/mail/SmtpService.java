package tback.kicketingback.user.signup.mail;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmtpService {

	private final SmtpMailSender smtpMailSender;

	public void sendVerification(String email, String code) {
		Mail mail = Mail.verification(email, code);
		smtpMailSender.send(mail);
		log.info("Verification email sent to {}", email);
	}

	public void sendRandomPassword(String email, String password) {
		Mail mail = Mail.randomPassword(email, password);
		smtpMailSender.send(mail);
		log.info("Random Password email sent to {}", email);
	}
}
