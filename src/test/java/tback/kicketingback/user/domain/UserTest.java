package tback.kicketingback.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static tback.kicketingback.global.encode.PasswordEncoderSHA256.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tback.kicketingback.performance.repository.ReservationRepositoryCustom;
import tback.kicketingback.user.exception.exceptions.AuthInvalidEmailException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidNameException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.user.exception.exceptions.AuthInvalidStateException;
import tback.kicketingback.user.exception.exceptions.EmailFormatException;
import tback.kicketingback.user.repository.FakeUserRepository;
import tback.kicketingback.user.service.UserService;
import tback.kicketingback.user.signup.mail.SmtpService;

public class UserTest {

	private UserService userService;
	private FakeUserRepository fakeUserRepository;
	private ReservationRepositoryCustom reservationRepositoryCustom;
	private SmtpService smtpService;

	@BeforeEach
	void initBefore() {
		userService = new UserService(fakeUserRepository, reservationRepositoryCustom, smtpService);
	}

	@Test
	@DisplayName("유저 생성 테스트")
	public void 유저_생성_테스트() {
		assertDoesNotThrow(() -> User.of("test@test.com", "123!@jjsjs4", "test", UserState.REGULAR_USER));
	}

	@ParameterizedTest
	@ValueSource(strings = {"testtest.com", "tes#R%estcom", "testtes*tcom"})
	@DisplayName("이메일 형식이 아니면 예외가 발생한다.")
	public void 이메일_형식이_아닐경우_예외(String email) {
		assertThrows(EmailFormatException.class, () -> User.of(email, "1234", "test", UserState.OAUTH_USER));
	}

	@Test
	@DisplayName("이름이 한글/영어가 아니면 예외가 발생한다: 한자")
	public void 이름이_유효하지_않으면_예외_발생_한자() {
		assertThrows(AuthInvalidNameException.class,
			() -> User.of("test@test.com", "test!test12", "習近平", UserState.REGULAR_USER));
	}

	@Test
	@DisplayName("이름이 한글/영어가 아니면 예외가 발생한다: 한글")
	public void 이름이_유효하지_않으면_예외_발생_한글() {
		assertDoesNotThrow(
			() -> User.of("test@test.com", "test!test12", "시진핑", UserState.OAUTH_USER));
	}

	@Test
	@DisplayName("이름이 한글/영어가 아니면 예외가 발생한다: 영어")
	public void 이름이_유효하지_않으면_예외_발생_영어() {
		assertDoesNotThrow(
			() -> User.of("test@test.com", "test!test12", "jinping", UserState.REGULAR_USER));
	}

	@Test
	@DisplayName("비밀번호가 user의 비밀번호와 일치하면 예외가 발생하지 않는다.")
	public void 비밀번호가_일치하면_예외_발생하지_않음() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.OAUTH_USER);

		assertDoesNotThrow(() -> user.validatePassword("1234abc!@"));
	}

	@Test
	@DisplayName("비밀번호가 user의 비밀번호와 일치하지 않으면 예외가 발생한다.")
	public void 비밀번호가_일치하지_않으면_예외_발생() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.REGULAR_USER);

		assertThrows(AuthInvalidPasswordException.class, () -> user.validatePassword(""));
	}

	@Test
	@DisplayName("이메일이 user의 이메일과 일치하면 예외가 발생하지 않는다.")
	public void 이메일이_일치하면_예외_발생하지_않음() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.OAUTH_USER);

		assertDoesNotThrow(() -> user.validateEmail("test@test.com"));
	}

	@Test
	@DisplayName("이메일이 user의 이메일과 일치하지 않으면 예외가 발생한다.")
	public void 이메일이_일치하지_않으면_예외_발생() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.REGULAR_USER);
		assertThrows(AuthInvalidEmailException.class, () -> user.validateEmail("nontest@test.com"));
	}

	@Test
	@DisplayName("유저 비밀 번호 변경 테스트")
	public void 비밀번호_변경_테스트() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.OAUTH_USER);
		String confirmPassword = "1234abc!@";
		String newPassword = "1234abc!@1";

		userService.matchPassword(user, confirmPassword);
		userService.changePassword(user, newPassword);

		assertThat(user.getPassword()).isEqualTo(encode(newPassword));
	}

	@Test
	@DisplayName("유저 비밀 번호 변경 시 잘못된 형식은 예외를 터트린다.")
	public void 비밀번호_변경_테스트_잘못된_형식() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.REGULAR_USER);
		String newPassword = "1234abc";

		assertThrows(AuthInvalidPasswordException.class, () -> userService.changePassword(user, newPassword));
	}

	@Test
	@DisplayName("주소를 등록할 수 있다.")
	public void 주소_테스트() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.OAUTH_USER);
		String newAddress = "경성대학교 8호관 321호";
		userService.updateAddress(user, newAddress);
		assertThat(user.getAddress()).isEqualTo(newAddress);
	}

	@Test
	@DisplayName("등록된 주소를 변경할 수 있다.")
	public void 주소_변경_테스트() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.REGULAR_USER);
		String defaultAddress = "경성대학교 8호관 321호";
		String newAddress = "경성대학교 8호관 313호";
		userService.updateAddress(user, defaultAddress);
		userService.updateAddress(user, newAddress);
		assertThat(user.getAddress()).isEqualTo(newAddress);
	}

	@Test
	@DisplayName("비밀번호 찾기는 클라이언트가 보낸 이메일과 이름이 일치해야 한다.")
	public void 비밀번호_찾기_테스트() {
		User user = User.of("test@test.com", "1234abc!@", "test", UserState.OAUTH_USER);
		String emailReq = "test@test.com";
		String nameReq = "test";

		assertDoesNotThrow(() -> userService.matchInform(user, emailReq, nameReq));
	}

	@Test
	@DisplayName("OAuth로 가입한 유저는 1회에 한하여 이름을 변경할 수 있다.")
	public void 이름_변경_테스트() {
		User user = User.of("test@gmail.com", "1234abc!@", "푸바오", UserState.OAUTH_USER);
		String newName = "김철수";

		assertDoesNotThrow(() -> userService.updateName(user, newName));
	}

	@Test
	@DisplayName("일반회원(Regular)이 이름을 변경하면 예외를 터트린다.")
	public void 이름_변경_권한_없음() {
		User user = User.of("test@gmail.com", "1234abc!@", "푸바오", UserState.REGULAR_USER);
		String newName = "Kevin";

		assertThrows(AuthInvalidStateException.class, () -> userService.updateName(user, newName));
	}

	@Test
	@DisplayName("OAuth 가입 유저여도 변경할 이름 형식에 맞지 않으면 예외를 터트린다.")
	public void 이름_변경_형식_불일치() {
		User user = User.of("test@gmail.com", "1234abc!@", "푸바오", UserState.OAUTH_USER);
		String newName = "O M G";

		assertThrows(AuthInvalidNameException.class, () -> userService.updateName(user, newName));
	}

}