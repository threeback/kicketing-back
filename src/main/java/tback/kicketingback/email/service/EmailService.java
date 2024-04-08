package tback.kicketingback.email.service;

import jakarta.mail.internet.MimeMessage;

public interface EmailService {

	String createBody(String... args);

	MimeMessage createMail(String email, String body);

	void sendMail(MimeMessage message);
}
