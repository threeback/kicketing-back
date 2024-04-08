package tback.kicketingback.user.signup.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.email.service.EmailService;
import tback.kicketingback.global.repository.RedisRepository;
import tback.kicketingback.user.exception.exceptions.EmailCreateException;
import tback.kicketingback.user.exception.exceptions.EmailSendException;

@Service
public class SignUpEmailService implements EmailService, EmailAuthService {
	private final JavaMailSender javaMailSender;
	private final RedisRepository signupRedisRepository;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Value("${spring.data.redis.timeout.signup}")
	private int expireTime;

	public SignUpEmailService(
		JavaMailSender javaMailSender,
		@Qualifier("signupRedisRepository") RedisRepository signupRedisRepository
	) {
		this.javaMailSender = javaMailSender;
		this.signupRedisRepository = signupRedisRepository;
	}

	@Override
	public MimeMessage createMail(String email, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.addRecipients(MimeMessage.RecipientType.TO, email);
			message.setSubject("Kicketing 회원가입을 위한 이메일 인증");  // 제목 설정
			message.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new EmailCreateException();
		}

		return message;
	}

	public String createBody(int number) {
		StringBuilder body = new StringBuilder();
		body.append("<h1>" + "Welcome to Kicketing!" + "</h1>");
		body.append("<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>");
		body.append("<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>");

		body.append("<div align='center' style='border:1px solid black; font-family:verdana;'>");
		body.append("<h2>" + "회원가입 인증 코드입니다." + "</h2>");
		body.append("<h1 style='color:blue'>" + number + "</h1>");
		body.append("</div><br>");
		body.append("<h3>" + "감사합니다." + "</h3>");
		return body.toString();
	}

	@Override
	@Transactional
	public void sendMail(MimeMessage message, int number) {
		// 실제 메일 전송
		try {
			javaMailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
			throw new EmailSendException();
		}

		try {
			String email = message.getRecipients(MimeMessage.RecipientType.TO)[0].toString();
			signupRedisRepository.setValues(email, String.valueOf(number), Duration.ofMillis(expireTime));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean emailVerificationCode() {
		return false;
	}

}
