package tback.kicketingback.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseNaverUser(
	@JsonProperty("resultcode") String resultCode,
	@JsonProperty("message") String message,
	@JsonProperty("response") NaverUserInfo naverUserInfo) {
}
