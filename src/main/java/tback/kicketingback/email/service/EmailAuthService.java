package tback.kicketingback.email.service;

public interface EmailAuthService {
	void saveCode(String email, String code);

	boolean isCompleteEmailAuth(String email);

	void checkCode(String email, String inputCode);
}
