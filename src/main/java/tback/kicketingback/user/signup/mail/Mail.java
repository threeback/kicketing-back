package tback.kicketingback.user.signup.mail;

import static tback.kicketingback.user.signup.mail.HtmlEmailTemplate.MailType.*;

import lombok.Getter;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;

@Getter
public class Mail {

	private final String to;
	private final String subject;
	private final String content;

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	private Mail(String to, String subject, String content) {
		validationTo(to);
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public static Mail verification(String to, String certification) {
		return new Mail(to, EMAIL_VERIFICATION.subject(), EMAIL_VERIFICATION.content(certification));
	}

	public static Mail randomPassword(String to, String certification) {
		return new Mail(to, EMAIL_RANDOM_PASSWORD.subject(), EMAIL_RANDOM_PASSWORD.content(certification));
	}

	private void validationTo(String to) {
		if (!isValidEmailFormat(to)) {
			throw new EmailFormatException();
		}
	}

	private boolean isValidEmailFormat(String email) {
		return email.matches(EMAIL_REGEX);
	}
}
