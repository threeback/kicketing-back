package tback.kicketingback.user.domain;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import tback.kicketingback.user.exception.exceptions.AlreadySamePasswordException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidNameException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.exception.exceptions.UserPasswordEmptyException;

@Entity
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 320)
	private String email;

	@Column(nullable = false, columnDefinition = "char(64)")
	private String password;

	@Column
	private String address;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, columnDefinition = "tinyint default 0")
	private Short state;

	@CreatedDate
	@Column(nullable = false)
	@Comment("생성일")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	@Comment("수정일")
	private LocalDateTime modifiedAt;

	protected User() {
	}

	private User(final Long id, final String email, final String password, final String name) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public final static String DEFAULT_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$";
	
	private static final String DEFAULT_EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private static final String DEFAULT_NAME_REGEX = "^[가-힣a-zA-Z]{2,20}$";

	public static User of(final String email, final String password, final String name) {
		validateCreateMember(email, password, name);
		return new User(null, email, password, name);
	}

	private static void validateCreateMember(final String email, final String password, final String name) {
		if (!isEmailFormat(email)) {
			throw new EmailFormatException();
		}

		if (isEmpty(password)) {
			throw new UserPasswordEmptyException();
		}

		if (!isNameFormat(name)) {
			throw new AuthInvalidNameException();
		}
	}

	private static boolean isEmailFormat(final String email) {
		return Pattern.matches(DEFAULT_EMAIL_REGEX, email);
	}

	private static boolean isNameFormat(final String name) {
		return Pattern.matches(DEFAULT_NAME_REGEX, name);
	}

	private static boolean isEmpty(final String password) {
		return password == null || password.isBlank();
	}

	public void validatePassword(final String password) {
		if (!this.password.equals(password)) {
			throw new AuthInvalidPasswordException();
		}
	}

	public void validateEmail(final String email) {
		if (!this.email.equals(email)) {
			throw new AuthInvalidEmailException();
		}
	}

	public void changePassword(final String newPassword) {
		isSamePassword(newPassword);
		this.password = newPassword;
	}

	private void isSamePassword(final String password) {
		if (this.password.equals(password)) {
			throw new AlreadySamePasswordException();
		}
	}
}
