package tback.kicketingback.user.util;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tback.kicketingback.auth.oauth.util.PasswordUtil;

public class PasswordUtilTest {

	@Test
	@DisplayName("발급한 비밀번호는 정규식을 만족한다. 100번 반복")
	void testCreateRandomPassword() {
		IntStream.range(1, 100).forEach((num) -> testOnetime());
	}

	private static void testOnetime() {
		String password = PasswordUtil.createRandomPassword();

		assertThatCode(() -> {
			PasswordUtil.isPasswordFormat(password);
		}).doesNotThrowAnyException();
	}
}
