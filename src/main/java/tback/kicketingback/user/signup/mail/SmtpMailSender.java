package tback.kicketingback.user.signup.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.user.exception.exceptions.EmailSendException;

@Component
@RequiredArgsConstructor
public class SmtpMailSender {

	@Value("${spring.mail.username}")
	private String senderEmail;

	private final JavaMailSender javaMailSender;

	@Async
	public void send(Mail mail) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		setContent(mail, helper);
		javaMailSender.send(message);
	}

	private void setContent(Mail mail, MimeMessageHelper helper) {
		try {
			helper.setTo(mail.getTo());
			helper.setFrom(senderEmail);
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getContent(), true);
		} catch (MessagingException exception) {
			throw new EmailSendException();
		}
	}
}
