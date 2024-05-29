package tback.kicketingback.performance.domain.type;

import java.util.function.Function;

import tback.kicketingback.performance.exception.exceptions.InvalidGetDiscountTypeException;

public enum DiscountType {
	NONE(price -> 0),
	STUDENT(price -> (int)(price * 0.1)),
	OLD(price -> (int)(price * 0.3)),
	PRONUNCIATION(price -> (int)(price * 0.5));

	private final Function<Integer, Integer> getDiscountAmountFunction;

	DiscountType(Function<Integer, Integer> getDiscountAmountFunction) {
		this.getDiscountAmountFunction = getDiscountAmountFunction;
	}

	public static DiscountType of(String discountTypeString) {
		DiscountType discountType;

		try {
			discountType = DiscountType.valueOf(discountTypeString.toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new InvalidGetDiscountTypeException(discountTypeString);
		}

		return discountType;
	}

	public int getDiscountAmount(int price) {
		return this.getDiscountAmountFunction.apply(price);
	}
}
