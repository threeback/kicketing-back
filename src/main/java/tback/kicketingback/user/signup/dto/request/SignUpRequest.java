package tback.kicketingback.user.signup.dto.request;

public record SignUpRequest(
        String username,
        String email,
        String password
) {
}
