package tback.kicketingback.email.service;

public interface EmailAuthService {
	
	String EMAIL_AUTH_ACCESS = "access";

	void expireEmailAuth(String email);

	void saveCode(String email, String code);

	boolean isCompleteEmailAuth(String email);

	void checkCode(String email, String inputCode);
}
