package tback.kicketingback.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.exception.exceptions.UserPasswordEmptyException;

public class UserTest {

	@Test
	@DisplayName("유저 생성 테스트")
	public void createUser() {
		assertDoesNotThrow(() -> User.of("test@test.com", "1234", "test"));
	}

	@Test
	@DisplayName("이메일 형식이 아니면 예외가 발생한다.")
	public void throwExceptionIfEmailIsNotValid() {
		assertThrows(EmailFormatException.class, () -> User.of("testtest.com", "1234", "test"));
	}

	@Test
	@DisplayName("비밀번호는 공백 문자가 아니어야 한다.")
	public void throwExceptionIfPasswordIsBlank() {
		assertThrows(UserPasswordEmptyException.class, () -> User.of("test@test.com", "", "test"));
	}

	@Test
	@DisplayName("비밀번호가 user의 비밀번호와 일치하면 예외가 발생하지 않는다.")
	public void throwExceptionIfPasswordIsMatch() {
		User user = User.of("test@test.com", "1234", "test");

		assertDoesNotThrow(() -> user.validatePassword("1234"));
	}

	@Test
	@DisplayName("비밀번호가 user의 비밀번호와 일치하지 않으면 예외가 발생한다.")
	public void throwExceptionIfPasswordIsNotMatch() {
		User user = User.of("test@test.com", "1234", "test");

		assertThrows(AuthInvalidPasswordException.class, () -> user.validatePassword(""));
	}

	@Test
	@DisplayName("이메일이 user의 이메일과 일치하면 예외가 발생하지 않는다.")
	public void throwExceptionIfEmailIsMatch() {
		User user = User.of("test@test.com", "1234", "test");

		assertDoesNotThrow(() -> user.validateEmail("test@test.com"));
	}

	@Test
	@DisplayName("이메일이 user의 이메일과 일치하지 않으면 예외가 발생한다.")
	public void throwExceptionIfEmailIsNotMatch() {
		User user = User.of("test@test.com", "1234", "test");
		assertThrows(AuthInvalidEmailException.class, () -> user.validateEmail("nontest@test.com"));
	}

	@Test
	@DisplayName("유저 비밀 번호 변경 테스트")
	public void changePassword() {
		User user = User.of("test@test.com", "1234", "test");
		String newPassword = "5678";
		user.changePassword(newPassword);

		assertThat(user.getPassword()).isEqualTo(newPassword);
	}
}
