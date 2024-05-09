package tback.kicketingback.user.dto;

public record PasswordRequest(String confirmPassword,
                              String newPassword) {
}
