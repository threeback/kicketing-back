package tback.kicketingback.user.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tback.kicketingback.utils.NumberCodeUtil;

public class NumberCodeUtilTest {

	@DisplayName("6자리 랜덤 코드 생성 테스트")
	@Test
	void 인증_코드_여섯_자리_생성_테스트() {
		String code = NumberCodeUtil.createRandomCode6();

		assertEquals(6, code.length());
	}

	@DisplayName("6자리 랜덤 코드가 숫자로만 이루어져 있는지 확인")
	@Test
	public void 인증_코드가_숫자로만_이루어져_있는지_확인() {
		String code = NumberCodeUtil.createRandomCode6();

		assertTrue(code.matches("\\d+"));
	}
}
