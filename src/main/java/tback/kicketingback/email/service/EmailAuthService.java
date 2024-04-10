package tback.kicketingback.email.service;

public interface EmailAuthService {

	String EMAIL_AUTH_ACCESS = "access";

	void expireEmailAuth(String email);

	void saveCode(String email, String code);

	void validateEmailAuthCompletion(String email);

	void validateEmailAuthAttempt(String email);

	void checkCode(String email, String inputCode);
}
