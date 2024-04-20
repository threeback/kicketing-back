package tback.kicketingback.user.signup.dto.request;

import jakarta.validation.constraints.NotNull;

public record EmailConfirmRequest(
	@NotNull(message = "이메일이 입력되지 않았습니다.")
	String email,
	@NotNull(message = "코드가 입력되지 않았습니다.")
	String code) {
}
