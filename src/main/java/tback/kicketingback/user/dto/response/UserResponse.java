package tback.kicketingback.user.dto.response;

import tback.kicketingback.user.domain.User;

public record UserResponse(String email, String name, String address) {
	private UserResponse(User user) {
		this(user.getEmail(), user.getName(), user.getAddress());
	}

	public static UserResponse from(User user) {
		return new UserResponse(user);
	}
}
