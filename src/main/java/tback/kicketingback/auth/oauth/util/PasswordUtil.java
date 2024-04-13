package tback.kicketingback.auth.oauth.util;

import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.utils.NumberCodeUtil;

public class PasswordUtil {
	private static final Random random = new Random();
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
	private final static String DEFAULT_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$";

	public static String createRandomPassword() {
		String randomString = IntStream.generate(() -> random.nextInt(0, CHARACTERS.length()))
			.limit(5)
			.mapToObj(CHARACTERS::charAt)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
		String randomCode6 = NumberCodeUtil.createRandomCode6();
		char randomSpecialChar = getRandomSpecialChar();

		String randomPassword = randomString + randomCode6 + randomSpecialChar;
		if (!isPasswordFormat(randomPassword)) {
			throw new AuthInvalidPasswordException();
		}
		return randomPassword;
	}

	public static boolean isPasswordFormat(final String password) {
		return Pattern.matches(DEFAULT_PASSWORD_REGEX, password);
	}

	private static char getRandomSpecialChar() {
		return SPECIAL_CHARACTERS.charAt(random.nextInt(0, SPECIAL_CHARACTERS.length()));
	}
}
