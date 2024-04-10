package tback.kicketingback.user.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tback.kicketingback.user.signup.utils.NumberUtil;

public class NumberUtilTest {

	@DisplayName("6자리 랜덤 코드가 생성되는지 테스트")
	@Test
	public void testCreateRandomCode6_Length() {
		String code = NumberUtil.createRandomCode6();

		assertEquals(6, code.length());
	}

	@DisplayName("6자리 랜덤 코드가 숫자로만 이루어져 있는지 확인")
	@Test
	public void testCreateRandomCode6_Content() {
		String code = NumberUtil.createRandomCode6();

		assertTrue(code.matches("\\d+"));
	}
}
