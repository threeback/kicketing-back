package tback.kicketingback.performance.exception.exceptions;

public class InvalidGetDiscountTypeException extends RuntimeException {
	public InvalidGetDiscountTypeException(String discountType) {
		super("%s : 유효하지 않은 할인대상".formatted(discountType));
	}
}
