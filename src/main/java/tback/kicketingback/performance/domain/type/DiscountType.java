package tback.kicketingback.performance.domain.type;

import tback.kicketingback.performance.exception.exceptions.InvalidGetDiscountTypeException;

public enum DiscountType {
	NONE, STUDENT, OLD, PRONUNCIATION;

	public static DiscountType of(String discountTypeString) {
		DiscountType discountType;

		try {
			discountType = DiscountType.valueOf(discountTypeString.toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new InvalidGetDiscountTypeException(discountTypeString);
		}

		return discountType;
	}
}
