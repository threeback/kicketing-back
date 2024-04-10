package tback.kicketingback.user.signup.utils;

public class NumberUtil {
	
	public static int createRandomCode6() {
		return (int)(Math.random() * (90000)) + 100000;
	}
}
