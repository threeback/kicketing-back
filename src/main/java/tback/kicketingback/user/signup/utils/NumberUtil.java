package tback.kicketingback.user.signup.utils;

public class NumberUtil {

	// 랜덤 인증 코드 생성
	public static int createRandomCode6() {
		return (int)(Math.random() * (90000)) + 100000;
	}
}
