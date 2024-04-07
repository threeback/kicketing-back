package tback.kicketingback.email.service;

import jakarta.mail.internet.MimeMessage;

public interface EmailService {
	MimeMessage createMail(String email);

	void sendMail(MimeMessage message);
}
