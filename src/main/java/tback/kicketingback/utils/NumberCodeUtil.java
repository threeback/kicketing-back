package tback.kicketingback.utils;

import java.security.SecureRandom;

public class NumberCodeUtil {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int START_ASCII = 48;
	private static final int END_ASCII = 58;
	private static final int CODE_LENGTH = 6;

	public static String createRandomCode6() {
		return RANDOM.ints(START_ASCII, END_ASCII)
			.limit(CODE_LENGTH)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
}
