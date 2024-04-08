package tback.kicketingback.email.service;

public interface EmailAuthService {
	void saveCode(String email, String code);

	boolean isCompleteEmailAuth(String email);

	boolean emailVerificationCode(String email, String inputCode);
}
