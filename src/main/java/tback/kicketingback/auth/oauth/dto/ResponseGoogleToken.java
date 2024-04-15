package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseGoogleToken(
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("refresh_token") String refreshToken,
	@JsonProperty("expires_in") String expiresIn,
	@JsonProperty("scope") String scope,
	@JsonProperty("token_type") String tokenType) {
}
