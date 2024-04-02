package tback.kicketingback.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidNameException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.exception.exceptions.UserPasswordEmptyException;

public class UserTest {

	@Test
	@DisplayName("유저 생성 테스트")
	public void createUser() {
		assertDoesNotThrow(() -> User.of("test@test.com", "123!@jjsjs4", "test"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"testtest.com", "tes#R%estcom", "testtes*tcom"})
	@DisplayName("이메일 형식이 아니면 예외가 발생한다.")
	public void throwExceptionIfEmailIsNotValid(String email) {
		assertThrows(EmailFormatException.class, () -> User.of(email, "1234", "test"));
	}

	@Test
	@DisplayName("비밀번호는 공백 문자가 아니어야 한다.")
	public void throwExceptionIfPasswordIsBlank() {
		assertThrows(UserPasswordEmptyException.class, () -> User.of("test@test.com", "", "test"));
	}

	@Test
	@DisplayName("비밀번호 정규식이 아니면 예외가 발생한다: 특수문자 포함 안 된 경우")
	public void throwExceptionIfPasswordIsNotValid() {
		assertThrows(AuthInvalidPasswordException.class, () -> User.of("test@test.com", "123test12", "test"));
	}

	@Test
	@DisplayName("비밀번호 정규식이 아니면 예외가 발생한다: 숫자 포함 안 된 경우")
	public void throwExceptionIfPasswordIsNotValid2() {
		assertThrows(AuthInvalidPasswordException.class, () -> User.of("test@test.com", "test@@@@@", "test"));
	}

	@Test
	@DisplayName("비밀번호 정규식이 아니면 예외가 발생한다: 영어 포함 안 된 경우")
	public void throwExceptionIfPasswordIsNotValid3() {
		assertThrows(AuthInvalidPasswordException.class, () -> User.of("test@test.com", "123123123!", "test"));
	}

	@Test
	@DisplayName("비밀번호 정규식이 아니면 예외가 발생한다: 길이가 안 된 경우")
	public void throwExceptionIfPasswordIsNotValid4() {
		assertThrows(AuthInvalidPasswordException.class, () -> User.of("test@test.com", "1a@", "test"));
	}

	@Test
	@DisplayName("비밀번호 정규식이 아니면 예외가 발생한다: 길이가 넘은 경우")
	public void throwExceptionIfPasswordIsNotValid5() {
		assertThrows(AuthInvalidPasswordException.class,
			() -> User.of("test@test.com", "1a@12312!@#!@!#!@#@!#@!2!@!!zda", "test"));
	}

	@Test
	@DisplayName("이름이 한글/영어가 아니면 예외가 발생한다.")
	public void throwExceptionIfNameIsNotValid() {
		assertThrows(AuthInvalidNameException.class,
			() -> User.of("test@test.com", "test!test12", "習近平"));
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
