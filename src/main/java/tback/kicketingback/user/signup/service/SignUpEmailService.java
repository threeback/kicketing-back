package tback.kicketingback.user.signup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import tback.kicketingback.email.service.EmailAuthService;
import tback.kicketingback.email.service.EmailService;
import tback.kicketingback.user.exception.exceptions.EmailCreateException;
import tback.kicketingback.user.exception.exceptions.EmailSendException;

@Service
@RequiredArgsConstructor
public class SignUpEmailService implements EmailService, EmailAuthService {
	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	private static int number;  // 랜덤 인증 코드

	// 랜덤 인증 코드 생성
	public static void createNumber() {
		number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
	}

	@Override
	public MimeMessage createMail(String email) {
		createNumber();  // 인증 코드 생성
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.addRecipients(MimeMessage.RecipientType.TO, email);
			message.setSubject("Kicketing 회원가입을 위한 이메일 인증");  // 제목 설정
			String body = "";
			body += "<h1>" + "Welcome to Kicketing!" + "</h1>";
			body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
			body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

			body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
			body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
			body += "<h1 style='color:blue'>" + number + "</h1>";
			body += "</div><br>";
			body += "<h3>" + "감사합니다." + "</h3>";
			message.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new EmailCreateException();
		}

		return message;
	}

	@Override
	public void sendMail(MimeMessage message) {
		// 실제 메일 전송
		try {
			javaMailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
			throw new EmailSendException();
		}

	}

	@Override
	public boolean emailVerificationCode() {
		return false;
	}
}
