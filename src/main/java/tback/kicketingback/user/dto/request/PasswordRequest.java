package tback.kicketingback.user.dto.request;

public record PasswordRequest(String confirmPassword,
							  String newPassword) {
}
