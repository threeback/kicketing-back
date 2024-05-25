package tback.kicketingback.performance.domain.type;

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
}