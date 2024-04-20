package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseGoogleUser(
	@JsonProperty("id") String id,
	@JsonProperty("email") String email,
	@JsonProperty("name") String name) {
}
