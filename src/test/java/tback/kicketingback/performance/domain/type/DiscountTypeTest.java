package tback.kicketingback.performance.domain.type;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import tback.kicketingback.performance.exception.exceptions.InvalidGetDiscountTypeException;

@SpringBootTest
class DiscountTypeTest {

	@Test
	@DisplayName("[실패] 포함되지 않는 할인 대상")
	public void 할인_대상_아님() {
		String discountType = "GOD";
		assertThrows(InvalidGetDiscountTypeException.class,
			() -> DiscountType.of(discountType));
	}

	@Test
	@DisplayName("[성공] 할인 대상에 포함")
	public void 할인_대상_맞음() {
		String discountType = "STUDENT";
		assertDoesNotThrow(() -> DiscountType.of(discountType));
	}

	@Test
	@DisplayName("[실패] 잘못된 가격")
	public void 가격_안맞음() {
		DiscountType discountType = DiscountType.PRONUNCIATION;
		int invalidPrice = 100;
		int defaultPrice = 16000;

		assertThat(discountType.getDiscountAmount(defaultPrice)).isNotEqualTo(invalidPrice);
	}

	@Test
	@DisplayName("[성공] 올바른 계산")
	public void 가격_맞음() {
		DiscountType discountType = DiscountType.PRONUNCIATION;
		int validPrice = 8000;
		int defaultPrice = 16000;

		assertThat(discountType.getDiscountAmount(defaultPrice)).isEqualTo(validPrice);
	}
}