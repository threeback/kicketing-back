package tback.kicketingback.global.encode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordEncoderSHA256Test {

	@Test
	@DisplayName("SHA-256 암호화 제대로 되었는지 확인함.")
	public void 인코딩_테스트() {
		// Given

		final String rawPassword = "1234abc!@";

		// When
		String encoded = PasswordEncoderSHA256.encode(rawPassword);

		// Then
		Assertions.assertThat(encoded).isEqualTo(PasswordEncoderSHA256.encode(rawPassword));
	}
}
