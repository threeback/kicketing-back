package tback.kicketingback.user.signup.utils;

public class NumberUtil {

	// 랜덤 인증 코드 생성
	public static int createNumber() {
		return (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
	}
}
