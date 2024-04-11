package tback.kicketingback.auth.oauth.util;

import java.security.SecureRandom;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import tback.kicketingback.user.exception.exceptions.AuthInvalidPasswordException;
import tback.kicketingback.utils.NumberCodeUtil;

public class PasswordUtil {
	private static final SecureRandom random = new SecureRandom();
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
	private final static String DEFAULT_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,12}$";

	public static String createRandomPassword() {
		String randomString = IntStream.rangeClosed(0, CHARACTERS.length())
			.mapToObj(CHARACTERS::charAt)
			.toString();
		String randomCode6 = NumberCodeUtil.createRandomCode6();
		String randomSpecialChar = getRandomSpecialChar();

		String randomPassword = randomString + randomCode6 + randomSpecialChar;
		if (isPasswordFormat(randomPassword)) {
			throw new AuthInvalidPasswordException();
		}
		return randomPassword;
	}

	public static boolean isPasswordFormat(final String password) {
		return Pattern.matches(DEFAULT_PASSWORD_REGEX, password);
	}

	private static String getRandomSpecialChar() {
		return IntStream.rangeClosed(0, SPECIAL_CHARACTERS.length())
			.mapToObj(SPECIAL_CHARACTERS::charAt)
			.toString();
	}
}
