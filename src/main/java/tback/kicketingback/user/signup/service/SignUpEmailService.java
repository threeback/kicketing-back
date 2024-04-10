package tback.kicketingback.user.signup.service;

import java.time.Duration;
import java.util.Optional;

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
import tback.kicketingback.user.exception.exceptions.AlreadyEmailAuthCompleteException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.EmailAuthIncompleteException;
import tback.kicketingback.user.exception.exceptions.EmailCreateException;
import tback.kicketingback.user.exception.exceptions.EmailSendException;
import tback.kicketingback.user.exception.exceptions.MismatchEmailAuthCodeException;

@Service
public class SignUpEmailService implements EmailService, EmailAuthService {
	public static final String AUTH_CODE_EMAIL_SUBJECT = "Kicketing 회원가입을 위한 이메일 인증";
	private final JavaMailSender javaMailSender;
	private final RedisRepository signupRedisRepository;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Value("${spring.data.redis.timeout.signup.code}")
	private int codeExpireTime;

	@Value("${spring.data.redis.timeout.signup.access}")
	private int accessExpireTime;

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
			message.setSubject(AUTH_CODE_EMAIL_SUBJECT);
			message.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new EmailCreateException();
		}

		return message;
	}

	@Override
	public String createBody(String... args) {
		String email = args[0];
		String code = args[1];

		StringBuilder body = new StringBuilder();
		body.append("<h1> Welcome to Kicketing! </h1>");
		body.append("<h3> %s 회원가입을 위한 요청하신 인증 번호입니다. </h3><br>".formatted(email));
		body.append("<h2> 아래 코드를 회원가입 창으로 돌아가 입력해주세요. </h2>");
		body.append("<div align='center' style='border:1px solid black; font-family:verdana;'>");
		body.append("<h2> 회원가입 인증 코드입니다. </h2>");
		body.append("<h1 style='color:blue'> %s </h1>".formatted(code));
		body.append("</div><br>");
		body.append("<h3> 감사합니다. </h3>");

		return body.toString();
	}

	@Override
	@Transactional
	public void sendMail(MimeMessage message) {
		try {
			javaMailSender.send(message);
		} catch (MailException e) {
			throw new EmailSendException();
		}
	}

	@Override
	public void saveCode(String email, String code) {
		signupRedisRepository.setValues(email, code, Duration.ofMillis(codeExpireTime));
	}

	@Override
	public void validateEmailAuthCompletion(String email) {
		Optional<String> state = signupRedisRepository.getValues(email);

		if (state.isEmpty()) {
			throw new AuthInvalidEmailException();
		}
		if (state.get().equals(EMAIL_AUTH_ACCESS)) {
			throw new AlreadyEmailAuthCompleteException();
		}
	}

	@Override
	public void validateEmailAuthAttempt(String email) {
		Optional<String> state = signupRedisRepository.getValues(email);

		if (state.isEmpty() || !state.get().equals(EMAIL_AUTH_ACCESS)) {
			throw new EmailAuthIncompleteException();
		}
	}

	@Override
	public void checkCode(String email, String inputCode) {
		String code = signupRedisRepository.getValues(email).orElseThrow(AuthInvalidEmailException::new);

		if (!code.equals(inputCode)) {
			throw new MismatchEmailAuthCodeException();
		}

		signupRedisRepository.setValues(email, EMAIL_AUTH_ACCESS, Duration.ofMillis(accessExpireTime));
	}

	@Override
	public void expireEmailAuth(String email) {
		signupRedisRepository.deleteValues(email);
	}
}
